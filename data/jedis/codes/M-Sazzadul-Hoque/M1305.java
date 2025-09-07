    @Override
    public AggregationResult build(Object data) {
      List<Object> list = (List<Object>) data;
      // return new AggregationResult(list.get(0), (long) list.get(1));
      AggregationResult r = SEARCH_AGGREGATION_RESULT.build(list.get(0));
      r.setCursorId((Long) list.get(1));
      return r;
    }
