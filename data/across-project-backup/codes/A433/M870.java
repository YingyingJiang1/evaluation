    @SuppressWarnings("MethodLength")
    public void build(final SharpStream output) throws IOException {
        if (done) {
            throw new RuntimeException("already done");
        }
        if (!output.canWrite()) {
            throw new IOException("the provided output is not writable");
        }

        //
        // WARNING: the muxer requires at least 8 samples of every track
        //          not allowed for very short tracks (less than 0.5 seconds)
        //
        outStream = output;
        long read = 8; // mdat box header size
        long totalSampleSize = 0;
        final int[] sampleExtra = new int[readers.length];
        final int[] defaultMediaTime = new int[readers.length];
        final int[] defaultSampleDuration = new int[readers.length];
        final int[] sampleCount = new int[readers.length];

        final TablesInfo[] tablesInfo = new TablesInfo[tracks.length];
        for (int i = 0; i < tablesInfo.length; i++) {
            tablesInfo[i] = new TablesInfo();
        }

        final int singleSampleBuffer;
        if (tracks.length == 1 && tracks[0].kind == TrackKind.Audio) {
            // near 1 second of audio data per chunk, avoid split the audio stream in large chunks
            singleSampleBuffer = tracks[0].trak.mdia.mdhdTimeScale / 1000;
        } else {
            singleSampleBuffer = -1;
        }


        for (int i = 0; i < readers.length; i++) {
            int samplesSize = 0;
            int sampleSizeChanges = 0;
            int compositionOffsetLast = -1;

            Mp4DashChunk chunk;
            while ((chunk = readers[i].getNextChunk(true)) != null) {

                if (defaultMediaTime[i] < 1 && chunk.moof.traf.tfhd.defaultSampleDuration > 0) {
                    defaultMediaTime[i] = chunk.moof.traf.tfhd.defaultSampleDuration;
                }

                read += chunk.moof.traf.trun.chunkSize;
                sampleExtra[i] += chunk.moof.traf.trun.chunkDuration; // calculate track duration

                TrunEntry info;
                while ((info = chunk.getNextSampleInfo()) != null) {
                    if (info.isKeyframe) {
                        tablesInfo[i].stss++;
                    }

                    if (info.sampleDuration > defaultSampleDuration[i]) {
                        defaultSampleDuration[i] = info.sampleDuration;
                    }

                    tablesInfo[i].stsz++;
                    if (samplesSize != info.sampleSize) {
                        samplesSize = info.sampleSize;
                        sampleSizeChanges++;
                    }

                    if (info.hasCompositionTimeOffset) {
                        if (info.sampleCompositionTimeOffset != compositionOffsetLast) {
                            tablesInfo[i].ctts++;
                            compositionOffsetLast = info.sampleCompositionTimeOffset;
                        }
                    }

                    totalSampleSize += info.sampleSize;
                }
            }

            if (defaultMediaTime[i] < 1) {
                defaultMediaTime[i] = defaultSampleDuration[i];
            }

            readers[i].rewind();

            if (singleSampleBuffer > 0) {
                initChunkTables(tablesInfo[i], singleSampleBuffer, singleSampleBuffer);
            } else {
                initChunkTables(tablesInfo[i], SAMPLES_PER_CHUNK_INIT, SAMPLES_PER_CHUNK);
            }

            sampleCount[i] = tablesInfo[i].stsz;

            if (sampleSizeChanges == 1) {
                tablesInfo[i].stsz = 0;
                tablesInfo[i].stszDefault = samplesSize;
            } else {
                tablesInfo[i].stszDefault = 0;
            }

            if (tablesInfo[i].stss == tablesInfo[i].stsz) {
                tablesInfo[i].stss = -1; // for audio tracks (all samples are keyframes)
            }

            // ensure track duration
            if (tracks[i].trak.tkhd.duration < 1) {
                tracks[i].trak.tkhd.duration = sampleExtra[i]; // this never should happen
            }
        }


        final boolean is64 = read > THRESHOLD_FOR_CO64;

        // calculate the moov size
        final int auxSize = makeMoov(defaultMediaTime, tablesInfo, is64);

        if (auxSize < THRESHOLD_MOOV_LENGTH) {
            auxBuffer = ByteBuffer.allocate(auxSize); // cache moov in the memory
        }

        moovSimulation = false;
        writeOffset = 0;

        final int ftypSize = makeFtyp();

        // reserve moov space in the output stream
        if (auxSize > 0) {
            int length = auxSize;
            final byte[] buffer = new byte[64 * 1024]; // 64 KiB
            while (length > 0) {
                final int count = Math.min(length, buffer.length);
                outWrite(buffer, count);
                length -= count;
            }
        }

        if (auxBuffer == null) {
            outSeek(ftypSize);
        }

        // tablesInfo contains row counts
        // and after returning from makeMoov() will contain those table offsets
        makeMoov(defaultMediaTime, tablesInfo, is64);

        // write tables: stts stsc sbgp
        // reset for ctts table: sampleCount sampleExtra
        for (int i = 0; i < readers.length; i++) {
            writeEntryArray(tablesInfo[i].stts, 2, sampleCount[i], defaultSampleDuration[i]);
            writeEntryArray(tablesInfo[i].stsc, tablesInfo[i].stscBEntries.length,
                    tablesInfo[i].stscBEntries);
            tablesInfo[i].stscBEntries = null;
            if (tablesInfo[i].ctts > 0) {
                sampleCount[i] = 1; // the index is not base zero
                sampleExtra[i] = -1;
            }
            if (tablesInfo[i].sbgp > 0) {
                writeEntryArray(tablesInfo[i].sbgp, 1, sampleCount[i]);
            }
        }

        if (auxBuffer == null) {
            outRestore();
        }

        outWrite(makeMdat(totalSampleSize, is64));

        final int[] sampleIndex = new int[readers.length];
        final int[] sizes =
                new int[singleSampleBuffer > 0 ? singleSampleBuffer : SAMPLES_PER_CHUNK];
        final int[] sync = new int[singleSampleBuffer > 0 ? singleSampleBuffer : SAMPLES_PER_CHUNK];

        int written = readers.length;
        while (written > 0) {
            written = 0;

            for (int i = 0; i < readers.length; i++) {
                if (sampleIndex[i] < 0) {
                    continue; // track is done
                }

                final long chunkOffset = writeOffset;
                int syncCount = 0;
                final int limit;
                if (singleSampleBuffer > 0) {
                    limit = singleSampleBuffer;
                } else {
                    limit = sampleIndex[i] == 0 ? SAMPLES_PER_CHUNK_INIT : SAMPLES_PER_CHUNK;
                }

                int j = 0;
                for (; j < limit; j++) {
                    final Mp4DashSample sample = getNextSample(i);

                    if (sample == null) {
                        if (tablesInfo[i].ctts > 0 && sampleExtra[i] >= 0) {
                            writeEntryArray(tablesInfo[i].ctts, 1, sampleCount[i],
                                    sampleExtra[i]); // flush last entries
                            outRestore();
                        }
                        sampleIndex[i] = -1;
                        break;
                    }

                    sampleIndex[i]++;

                    if (tablesInfo[i].ctts > 0) {
                        if (sample.info.sampleCompositionTimeOffset == sampleExtra[i]) {
                            sampleCount[i]++;
                        } else {
                            if (sampleExtra[i] >= 0) {
                                tablesInfo[i].ctts = writeEntryArray(tablesInfo[i].ctts, 2,
                                        sampleCount[i], sampleExtra[i]);
                                outRestore();
                            }
                            sampleCount[i] = 1;
                            sampleExtra[i] = sample.info.sampleCompositionTimeOffset;
                        }
                    }

                    if (tablesInfo[i].stss > 0 && sample.info.isKeyframe) {
                        sync[syncCount++] = sampleIndex[i];
                    }

                    if (tablesInfo[i].stsz > 0) {
                        sizes[j] = sample.data.length;
                    }

                    outWrite(sample.data, sample.data.length);
                }

                if (j > 0) {
                    written++;

                    if (tablesInfo[i].stsz > 0) {
                        tablesInfo[i].stsz = writeEntryArray(tablesInfo[i].stsz, j, sizes);
                    }

                    if (syncCount > 0) {
                        tablesInfo[i].stss = writeEntryArray(tablesInfo[i].stss, syncCount, sync);
                    }

                    if (tablesInfo[i].stco > 0) {
                        if (is64) {
                            tablesInfo[i].stco = writeEntry64(tablesInfo[i].stco, chunkOffset);
                        } else {
                            tablesInfo[i].stco = writeEntryArray(tablesInfo[i].stco, 1,
                                    (int) chunkOffset);
                        }
                    }

                    outRestore();
                }
            }
        }

        if (auxBuffer != null) {
            // dump moov
            outSeek(ftypSize);
            outStream.write(auxBuffer.array(), 0, auxBuffer.capacity());
            auxBuffer = null;
        }
    }
