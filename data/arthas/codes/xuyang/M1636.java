    private static MapValue createMapValue(Map<?, ?> map, int depth) {
        MapValue.Builder builder = MapValue.newBuilder();

        for (Entry<?, ?> entry : map.entrySet()) {
            MapEntry mapEntry = MapEntry.newBuilder().setKey(toJavaObject(entry.getKey(), depth))
                    .setValue(toJavaObject(entry.getValue(), depth)).build();
            builder.addEntries(mapEntry);
        }
        return builder.build();
    }
