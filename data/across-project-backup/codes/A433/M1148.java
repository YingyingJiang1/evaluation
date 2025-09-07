    @Override
    public void seek(long offset) throws IOException {
        long total = out.length + aux.length;

        if (offset == total) {
            // do not ignore the seek offset if a underflow exists
            long relativeOffset = out.getOffset() + aux.getOffset();
            if (relativeOffset == total) {
                return;
            }
        }

        // flush everything, avoid any underflow
        flush();

        if (offset < 0 || offset > total) {
            throw new IOException("desired offset is outside of range=0-" + total + " offset=" + offset);
        }

        if (offset > out.length) {
            out.seek(out.length);
            aux.seek(offset - out.length);
        } else {
            out.seek(offset);
            aux.seek(0);
        }
    }
