    @Override
    public Object build(Object data) {
      if (data == null) return null;
      if (data instanceof List) return BuilderFactory.DOUBLE_LIST.build(data);
      return JSON_ARRAY.build(data);
    }
