    @Override
    public T remove(Object key) {
        if (key instanceof byte[]) return internalMap.remove(new ByteArrayWrapper((byte[]) key));
        return internalMap.remove(key);
    }
