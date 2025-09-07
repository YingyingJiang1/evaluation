            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                if (!whitelist.contains(desc.getName())) {
                    throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
                }
                return super.resolveClass(desc);
            }
