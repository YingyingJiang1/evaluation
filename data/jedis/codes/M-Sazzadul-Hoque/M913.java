    @Override
    public Map<String, TSMRangeElements> build(Object data) {
      List<KeyValue> dataList = (List<KeyValue>) data;
      Map<String, TSMRangeElements> map = new LinkedHashMap<>(dataList.size() / 2, 1f);
      for (KeyValue kv : dataList) {
        String key = BuilderFactory.STRING.build(kv.getKey());
        List<Object> valueList = (List<Object>) kv.getValue();
        TSMRangeElements elements;
        switch (valueList.size()) {
          case 3:
            List<Object> aggrMapObj = (List<Object>) valueList.get(1);
            KeyValue aggKV = (KeyValue) aggrMapObj.get(0);
            assert "aggregators".equalsIgnoreCase(BuilderFactory.STRING.build(aggKV.getKey()));
            elements = new TSMRangeElements(key,
                BuilderFactory.STRING_MAP.build(valueList.get(0)),
                ((List<Object>) aggKV.getValue()).stream().map(BuilderFactory.STRING::build)
                    .map(AggregationType::safeValueOf).collect(Collectors.toList()),
                TIMESERIES_ELEMENT_LIST.build(valueList.get(2)));
            break;
          case 4:
            List<KeyValue> rdcMapObj = (List<KeyValue>) valueList.get(1);
            assert "reducers".equalsIgnoreCase(BuilderFactory.STRING.build(rdcMapObj.get(0).getKey()));
            List<KeyValue> srcMapObj = (List<KeyValue>) valueList.get(2);
            assert "sources".equalsIgnoreCase(BuilderFactory.STRING.build(srcMapObj.get(0).getKey()));
            elements = new TSMRangeElements(key,
                BuilderFactory.STRING_MAP.build(valueList.get(0)),
                BuilderFactory.STRING_LIST.build(rdcMapObj.get(0).getValue()),
                BuilderFactory.STRING_LIST.build(srcMapObj.get(0).getValue()),
                TIMESERIES_ELEMENT_LIST.build(valueList.get(3)));
            break;
          default:
            throw new IllegalStateException();
        }
        map.put(key, elements);
      }
      return map;
    }
