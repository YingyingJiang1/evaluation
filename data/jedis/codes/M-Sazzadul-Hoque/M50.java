    @Override
    @SuppressWarnings("unchecked")
    public Map<byte[], byte[]> build(Object data) {
      final List<Object> list = (List<Object>) data;
      if (list.isEmpty()) return Collections.emptyMap();

      if (list.get(0) instanceof KeyValue) {
        final Map<byte[], byte[]> map = new JedisByteHashMap();
        final Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
          KeyValue kv = (KeyValue) iterator.next();
          map.put(BINARY.build(kv.getKey()), BINARY.build(kv.getValue()));
        }
        return map;
      } else {
        final Map<byte[], byte[]> map = new JedisByteHashMap();
        final Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
          map.put(BINARY.build(iterator.next()), BINARY.build(iterator.next()));
        }
        return map;
      }
    }
