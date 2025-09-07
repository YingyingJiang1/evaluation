    @Override
    public Map<String, TSMRangeElements> build(Object data) {
      return ((List<Object>) data).stream().map((tsObject) -> (List<Object>) tsObject)
          .map((tsList) -> new TSMRangeElements(BuilderFactory.STRING.build(tsList.get(0)),
              BuilderFactory.STRING_MAP_FROM_PAIRS.build(tsList.get(1)),
              TIMESERIES_ELEMENT_LIST.build(tsList.get(2))))
          .collect(Collectors.toMap(TSMRangeElements::getKey, Function.identity(),
              (x, y) -> x, LinkedHashMap::new));
    }
