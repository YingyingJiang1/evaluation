    @Override
    @SuppressWarnings("unchecked")
    public KeyValue<byte[], byte[]> build(Object data) {
      if (data == null) return null;
      List<Object> l = (List<Object>) data;
      return KeyValue.of(BINARY.build(l.get(0)), BINARY.build(l.get(1)));
    }
