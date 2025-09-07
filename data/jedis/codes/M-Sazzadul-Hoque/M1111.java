    @Override
    public List<JSONArray> build(Object data) {
      if (data == null) {
        return null;
      }
      List<Object> list = (List<Object>) data;
      return list.stream().map(o -> JSON_ARRAY.build(o)).collect(Collectors.toList());
    }
