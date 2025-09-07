    @Override
    @SuppressWarnings("unchecked")
    public KeyValue<byte[], Tuple> build(Object data) {
      if (data == null) return null;
      List<Object> l = (List<Object>) data;
      if (l.isEmpty()) return null;
      return KeyValue.of(BINARY.build(l.get(0)), new Tuple(BINARY.build(l.get(1)), DOUBLE.build(l.get(2))));
    }
