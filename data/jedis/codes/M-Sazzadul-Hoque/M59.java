    @Override
    @SuppressWarnings("unchecked")
    public KeyValue<String, List<String>> build(Object data) {
      if (data == null) return null;
      List<byte[]> l = (List<byte[]>) data;
      return new KeyValue<>(STRING.build(l.get(0)), STRING_LIST.build(l.get(1)));
    }
