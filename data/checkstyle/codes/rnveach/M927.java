    private static byte[] toByteArray(InputStream stream) throws IOException {
        final ByteArrayOutputStream content = new ByteArrayOutputStream();

        while (true) {
            final int size = stream.read(BUFFER);
            if (size == -1) {
                break;
            }

            content.write(BUFFER, 0, size);
        }

        return content.toByteArray();
    }
