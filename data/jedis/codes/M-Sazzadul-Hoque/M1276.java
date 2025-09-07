  private static Map<String, Object> makeFieldsMap(Map<String, Boolean> isDecode, Object data) {
    if (data == null) return null;

    final List<KeyValue> list = (List) data;

    Map<String, Object> map = new HashMap<>(list.size(), 1f);
    list.stream().filter((kv) -> (kv != null && kv.getKey() != null && kv.getValue() != null))
        .forEach((kv) -> {
          String key = BuilderFactory.STRING.build(kv.getKey());
          map.put(key,
              (Boolean.FALSE.equals(isDecode.get(key)) ? BuilderFactory.RAW_OBJECT
                  : BuilderFactory.AGGRESSIVE_ENCODED_OBJECT).build(kv.getValue()));
        });
    return map;
  }
