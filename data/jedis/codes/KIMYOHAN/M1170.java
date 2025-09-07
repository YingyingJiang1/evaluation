    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends byte[], ? extends T> m) {
        Iterator<?> iterator = m.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<? extends byte[], ? extends T> next = (Entry<? extends byte[], ? extends T>) iterator
                    .next();
            internalMap.put(new ByteArrayWrapper(next.getKey()), next.getValue());
        }
    }
