    @Override
    public Map.Entry<Long, byte[]> build(Object data) {
      List<Object> list = (List<Object>) data;
      return new KeyValue<>(BuilderFactory.LONG.build(list.get(0)), BuilderFactory.BINARY.build(list.get(1)));
    }
