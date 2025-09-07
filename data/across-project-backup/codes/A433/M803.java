    public void parse() throws IOException, NoSuchElementException {
        if (selectedTrack > -1) {
            return;
        }

        box = readBox(ATOM_FTYP);
        brands = parseFtyp(box);
        switch (brands[0]) {
            case BRAND_DASH:
            case BRAND_ISO5:// ¿why not?
                break;
            default:
                throw new NoSuchElementException(
                        "Not a MPEG-4 DASH container, major brand is not 'dash' or 'iso5' is "
                                + boxName(brands[0])
                );
        }

        Moov moov = null;
        int i;

        while (box.type != ATOM_MOOF) {
            ensure(box);
            box = readBox();

            switch (box.type) {
                case ATOM_MOOV:
                    moov = parseMoov(box);
                    break;
                case ATOM_SIDX:
                case ATOM_MFRA:
                    break;
            }
        }

        if (moov == null) {
            throw new IOException("The provided Mp4 doesn't have the 'moov' box");
        }

        tracks = new Mp4Track[moov.trak.length];

        for (i = 0; i < tracks.length; i++) {
            tracks[i] = new Mp4Track();
            tracks[i].trak = moov.trak[i];

            if (moov.mvexTrex != null) {
                for (final Trex mvexTrex : moov.mvexTrex) {
                    if (tracks[i].trak.tkhd.trackId == mvexTrex.trackId) {
                        tracks[i].trex = mvexTrex;
                    }
                }
            }

            switch (moov.trak[i].mdia.hdlr.subType) {
                case HANDLER_VIDE:
                    tracks[i].kind = TrackKind.Video;
                    break;
                case HANDLER_SOUN:
                    tracks[i].kind = TrackKind.Audio;
                    break;
                case HANDLER_SUBT:
                    tracks[i].kind = TrackKind.Subtitles;
                    break;
                default:
                    tracks[i].kind = TrackKind.Other;
                    break;
            }
        }

        backupBox = box;
    }
