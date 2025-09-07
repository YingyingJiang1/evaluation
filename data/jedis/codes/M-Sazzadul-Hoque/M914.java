    @Override
    public Map<String, TSMGetElement> build(Object data) {
      return ((List<Object>) data).stream().map((tsObject) -> (List<Object>) tsObject)
          .map((tsList) -> new TSMGetElement(BuilderFactory.STRING.build(tsList.get(0)),
              BuilderFactory.STRING_MAP_FROM_PAIRS.build(tsList.get(1)),
              TIMESERIES_ELEMENT.build(tsList.get(2))))
          .collect(Collectors.toMap(TSMGetElement::getKey, Function.identity()));
    }
