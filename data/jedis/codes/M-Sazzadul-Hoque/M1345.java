    @Override
    public Map<String, List<String>> build(Object data) {
      List list = (List) data;
      if (list.isEmpty()) return Collections.emptyMap();

      if (list.get(0) instanceof KeyValue) {
        return ((List<KeyValue>) data).stream().collect(Collectors.toMap(
            kv -> STRING.build(kv.getKey()), kv -> BuilderFactory.STRING_LIST.build(kv.getValue())));
      }

      Map<String, List<String>> dump = new HashMap<>(list.size() / 2, 1f);
      for (int i = 0; i < list.size(); i += 2) {
        dump.put(STRING.build(list.get(i)), BuilderFactory.STRING_LIST.build(list.get(i + 1)));
      }
      return dump;
    }
