    @Override
    public T get(Object key) {
        if (key instanceof byte[]) return internalMap.get(new ByteArrayWrapper((byte[]) key));
        return internalMap.get(key);
    }
