    @Override
    public Set<byte[]> keySet() {
        Set<byte[]> keySet = new HashSet<>();
        Iterator<ByteArrayWrapper> iterator = internalMap.keySet().iterator();
        while (iterator.hasNext()) {
            keySet.add(iterator.next().data);
        }
        return keySet;
    }
