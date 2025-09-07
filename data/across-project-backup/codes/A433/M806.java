    public Mp4DashChunk getNextChunk(final boolean infoOnly) throws IOException {
        final Mp4Track track = tracks[selectedTrack];

        while (stream.available()) {

            if (chunkZero) {
                ensure(box);
                if (!stream.available()) {
                    break;
                }
                box = readBox();
            } else {
                chunkZero = true;
            }

            switch (box.type) {
                case ATOM_MOOF:
                    if (moof != null) {
                        throw new IOException("moof found without mdat");
                    }

                    moof = parseMoof(box, track.trak.tkhd.trackId);

                    if (moof.traf != null) {

                        if (hasFlag(moof.traf.trun.bFlags, 0x0001)) {
                            moof.traf.trun.dataOffset -= box.size + 8;
                            if (moof.traf.trun.dataOffset < 0) {
                                throw new IOException("trun box has wrong data offset, "
                                        + "points outside of concurrent mdat box");
                            }
                        }

                        if (moof.traf.trun.chunkSize < 1) {
                            if (hasFlag(moof.traf.tfhd.bFlags, 0x10)) {
                                moof.traf.trun.chunkSize = moof.traf.tfhd.defaultSampleSize
                                        * moof.traf.trun.entryCount;
                            } else {
                                moof.traf.trun.chunkSize = (int) (box.size - 8);
                            }
                        }
                        if (!hasFlag(moof.traf.trun.bFlags, 0x900)
                                && moof.traf.trun.chunkDuration == 0) {
                            if (hasFlag(moof.traf.tfhd.bFlags, 0x20)) {
                                moof.traf.trun.chunkDuration = moof.traf.tfhd.defaultSampleDuration
                                        * moof.traf.trun.entryCount;
                            }
                        }
                    }
                    break;
                case ATOM_MDAT:
                    if (moof == null) {
                        throw new IOException("mdat found without moof");
                    }

                    if (moof.traf == null) {
                        moof = null;
                        continue; // find another chunk
                    }

                    final Mp4DashChunk chunk = new Mp4DashChunk();
                    chunk.moof = moof;
                    if (!infoOnly) {
                        chunk.data = stream.getView(moof.traf.trun.chunkSize);
                    }

                    moof = null;

                    stream.skipBytes(chunk.moof.traf.trun.dataOffset);
                    return chunk;
                default:
            }
        }

        return null;
    }
