    @Override
    public List<KeyValue<String, List<String>>> build(Object data) {
      List<Object> list = (List<Object>) data;
      return list.stream().map(KEYED_STRING_LIST::build).collect(Collectors.toList());
    }
