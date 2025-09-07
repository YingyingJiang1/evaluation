    private Moov parseMoov(final Box ref) throws IOException {
        Box b = readBox(ATOM_MVHD);
        final Moov moov = new Moov();
        moov.mvhd = parseMvhd();
        ensure(b);

        final ArrayList<Trak> tmp = new ArrayList<>((int) moov.mvhd.nextTrackId);
        while ((b = untilBox(ref, ATOM_TRAK, ATOM_MVEX)) != null) {

            switch (b.type) {
                case ATOM_TRAK:
                    tmp.add(parseTrak(b));
                    break;
                case ATOM_MVEX:
                    moov.mvexTrex = parseMvex(b, (int) moov.mvhd.nextTrackId);
                    break;
            }

            ensure(b);
        }

        moov.trak = tmp.toArray(new Trak[0]);

        return moov;
    }
