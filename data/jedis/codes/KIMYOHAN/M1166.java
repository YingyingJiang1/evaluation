    @Override
    public boolean containsKey(Object key) {
        if (key instanceof byte[]) return internalMap.containsKey(new ByteArrayWrapper((byte[]) key));
        return internalMap.containsKey(key);
    }
