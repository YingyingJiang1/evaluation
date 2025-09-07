    static Map<String, String> getHttpHeadersFromMetadata(Metadata trailer) {
        Map<String, String> map = new HashMap<>();
        for (String key : trailer.keys()) {
            if (EXCLUDED.contains(key.toLowerCase())) {
                continue;
            }
            if (key.endsWith(Metadata.BINARY_HEADER_SUFFIX)) {
                // TODO allow any object type here
                byte[] value = trailer.get(Metadata.Key.of(key, Metadata.BINARY_BYTE_MARSHALLER));
                map.put(key, new String(value));
            } else {
                String value = trailer.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER));
                map.put(key, value);
            }
        }
        return map;
    }
