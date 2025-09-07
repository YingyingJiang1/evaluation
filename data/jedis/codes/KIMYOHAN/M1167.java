    @Override
    public Set<Entry<byte[], T>> entrySet() {
        Iterator<Entry<ByteArrayWrapper, T>> iterator = internalMap.entrySet()
                .iterator();
        HashSet<Entry<byte[], T>> hashSet = new HashSet<>();
        while (iterator.hasNext()) {
            Entry<ByteArrayWrapper, T> entry = iterator.next();
            hashSet.add(new JedisByteEntry(entry.getKey().data, entry.getValue()));
        }
        return hashSet;
    }
