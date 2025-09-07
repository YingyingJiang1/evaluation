    private static CollectionValue createCollectionValue(Collection<?> collection, int depth) {
        Builder builder = CollectionValue.newBuilder();
        for (Object o : collection) {
            builder.addElements(toJavaObject(o, depth));
        }
        return builder.build();
    }
