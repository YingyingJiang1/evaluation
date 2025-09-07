    public int read(final byte[] buffer, final int off, final int c) throws IOException {
        int offset = off;
        int count = c;

        if (readCount < 0) {
            return -1;
        }
        int total = 0;

        if (count >= readBuffer.length) {
            if (readCount > 0) {
                System.arraycopy(readBuffer, readOffset, buffer, offset, readCount);
                readOffset += readCount;

                offset += readCount;
                count -= readCount;

                total = readCount;
                readCount = 0;
            }
            total += Math.max(stream.read(buffer, offset, count), 0);
        } else {
            while (count > 0 && !fillBuffer()) {
                final int read = Math.min(readCount, count);
                System.arraycopy(readBuffer, readOffset, buffer, offset, read);

                readOffset += read;
                readCount -= read;

                offset += read;
                count -= read;

                total += read;
            }
        }

        position += total;
        return total;
    }
