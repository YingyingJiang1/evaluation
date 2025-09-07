    private Mdia parseMdia(final Box ref) throws IOException {
        final Mdia obj = new Mdia();

        Box b;
        while ((b = untilBox(ref, ATOM_MDHD, ATOM_HDLR, ATOM_MINF)) != null) {
            switch (b.type) {
                case ATOM_MDHD:
                    obj.mdhd = readFullBox(b);

                    // read time scale
                    final ByteBuffer buffer = ByteBuffer.wrap(obj.mdhd);
                    final byte version = buffer.get(8);
                    buffer.position(12 + ((version == 0 ? 4 : 8) * 2));
                    obj.mdhdTimeScale = buffer.getInt();
                    break;
                case ATOM_HDLR:
                    obj.hdlr = parseHdlr(b);
                    break;
                case ATOM_MINF:
                    obj.minf = parseMinf(b);
                    break;
            }
            ensure(b);
        }

        return obj;
    }
