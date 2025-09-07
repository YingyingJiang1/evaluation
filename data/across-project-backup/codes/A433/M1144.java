    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return;
        }

        long available;
        long offsetOut = out.getOffset();
        long offsetAux = aux.getOffset();
        long end = callback.check();

        if (end == -1) {
            available = Integer.MAX_VALUE;
        } else if (end < offsetOut) {
            throw new IOException("The reported offset is invalid: " + end + "<" + offsetOut);
        } else {
            available = end - offsetOut;
        }

        boolean usingAux = aux.length > 0 && offsetOut >= out.length;
        boolean underflow = offsetAux < aux.length || offsetOut < out.length;

        if (usingAux) {
            // before continue calculate the final length of aux
            long length = offsetAux + len;
            if (underflow) {
                if (aux.length > length) {
                    length = aux.length;// the length is not changed
                }
            } else {
                length = aux.length + len;
            }

            aux.write(b, off, len);

            if (length >= THRESHOLD_AUX_LENGTH && length <= available) {
                flushAuxiliar(available);
            }
        } else {
            if (underflow) {
                available = out.length - offsetOut;
            }

            int length = Math.min(len, (int) Math.min(Integer.MAX_VALUE, available));
            out.write(b, off, length);

            len -= length;
            off += length;

            if (len > 0) {
                aux.write(b, off, len);
            }
        }

        if (onProgress != null) {
            long absoluteOffset = out.getOffset() + aux.getOffset();
            if (absoluteOffset > reportPosition) {
                reportPosition = absoluteOffset + NOTIFY_BYTES_INTERVAL;
                onProgress.report(absoluteOffset);
            }
        }
    }
