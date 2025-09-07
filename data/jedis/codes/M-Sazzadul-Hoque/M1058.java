    @Override
    public List<T> build(Object data) {
      if (data == null) {
        return null;
      }
      List<String> list = BuilderFactory.STRING_LIST.build(data);
      return list.stream().map(s -> getJsonObjectMapper().fromJson(s, clazz)).collect(Collectors.toList());
    }
