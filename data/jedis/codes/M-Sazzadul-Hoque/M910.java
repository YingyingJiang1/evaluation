    @Override
    public TSElement build(Object data) {
      List<Object> list = (List<Object>) data;
      if (list == null || list.isEmpty()) return null;
      return new TSElement(BuilderFactory.LONG.build(list.get(0)), BuilderFactory.DOUBLE.build(list.get(1)));
    }
