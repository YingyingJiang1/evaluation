	public static byte[] readLine(InputStream in) throws IOException {
        int b;
        while (true) {
            b = in.read();
            if (b < 0) {
                return null;
            }
            if (b != LF) {
                break;
            }
        }

        int capacity = 256;
        byte[] buffer = new byte[capacity];
        int pos = 0;

        while (b != LF && b != CR && b != -1) {
            if (pos >= buffer.length) {
                capacity += 256;
                byte[] newBuffer = new byte[capacity];
                System.arraycopy(buffer, 0, newBuffer, 0, pos);
                buffer = newBuffer;
            }
            buffer[pos++] = (byte) b;
            b = in.read();
        }

        if (b == CR) {
            if (in.markSupported()) {
                in.mark(1);
                int next = in.read();
                if (next != LF) {
                    in.reset();
                }
            }
        }

        if (pos == 0 && b == -1) {
            return null;
        }

        return Arrays.copyOf(buffer, pos);
    }
