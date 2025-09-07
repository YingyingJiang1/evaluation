    public static byte[] toBytes(SimpleHttpResponse response) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(response);
            out.flush();
            return bos.toByteArray();
        }
    }
