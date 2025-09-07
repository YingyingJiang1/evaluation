    @Override
    @SuppressWarnings("unchecked")
    public KeyValue<byte[], List<byte[]>> build(Object data) {
      if (data == null) return null;
      List<byte[]> l = (List<byte[]>) data;
      return new KeyValue<>(BINARY.build(l.get(0)), BINARY_LIST.build(l.get(1)));
    }
