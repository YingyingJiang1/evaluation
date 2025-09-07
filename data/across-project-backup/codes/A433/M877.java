    private int make(final int type, final int extra, final int columns, final int rows)
            throws IOException {
        final byte base = 16;
        final int size = columns * rows * 4;
        int total = size + base;
        int offset = auxOffset();

        if (extra >= 0) {
            total += 4;
        }

        auxWrite(ByteBuffer.allocate(12)
                .putInt(total)
                .putInt(type)
                .putInt(0x00)// default version & flags
                .array()
        );

        if (extra >= 0) {
            offset += 4;
            auxWrite(extra);
        }

        auxWrite(rows);
        auxSkip(size);

        return offset + base;
    }
