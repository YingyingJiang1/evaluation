    @Override
    public Map<String, TSMGetElement> build(Object data) {
      List<KeyValue> dataList = (List<KeyValue>) data;
      Map<String, TSMGetElement> map = new LinkedHashMap<>(dataList.size());
      for (KeyValue kv : dataList) {
        String key = BuilderFactory.STRING.build(kv.getKey());
        List<Object> valueList = (List<Object>) kv.getValue();
        TSMGetElement value = new TSMGetElement(key,
            BuilderFactory.STRING_MAP.build(valueList.get(0)),
            TIMESERIES_ELEMENT.build(valueList.get(1)));
        map.put(key, value);
      }
      return map;
    }
