    @Override
    public SearchResult build(Object data) {
      List<KeyValue> list = (List<KeyValue>) data;
      long totalResults = -1;
      List<Document> results = null;
      List<String> warnings = null;
      for (KeyValue kv : list) {
        String key = BuilderFactory.STRING.build(kv.getKey());
        Object rawVal = kv.getValue();
        switch (key) {
          case TOTAL_RESULTS_STR:
            totalResults = BuilderFactory.LONG.build(rawVal);
            break;
          case RESULTS_STR:
            results = ((List<Object>) rawVal).stream()
                .map(documentBuilder::build)
                .collect(Collectors.toList());
            break;
          case WARNINGS_STR:
            warnings = BuilderFactory.STRING_LIST.build(rawVal);
            break;
        }
      }
      return new SearchResult(totalResults, results, warnings);
    }
