    @Override
    @SuppressWarnings("unchecked")
    public KeyValue<byte[], List<Tuple>> build(Object data) {
      if (data == null) return null;
      List<Object> l = (List<Object>) data;
      return new KeyValue<>(BINARY.build(l.get(0)), TUPLE_LIST_FROM_PAIRS.build(l.get(1)));
    }
