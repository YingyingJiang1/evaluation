    public static SimpleHttpResponse fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try (ObjectInputStream in = new ObjectInputStream(bis) {
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                if (!whitelist.contains(desc.getName())) {
                    throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
                }
                return super.resolveClass(desc);
            }
        }) {
            return (SimpleHttpResponse) in.readObject();
        }
    }
