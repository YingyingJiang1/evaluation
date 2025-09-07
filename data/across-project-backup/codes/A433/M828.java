    private Minf parseMinf(final Box ref) throws IOException {
        final Minf obj = new Minf();

        Box b;
        while ((b = untilAnyBox(ref)) != null) {

            switch (b.type) {
                case ATOM_DINF:
                    obj.dinf = readFullBox(b);
                    break;
                case ATOM_STBL:
                    obj.stblStsd = parseStbl(b);
                    break;
                case ATOM_VMHD:
                case ATOM_SMHD:
                    obj.mhd = readFullBox(b);
                    break;

            }
            ensure(b);
        }

        return obj;
    }
