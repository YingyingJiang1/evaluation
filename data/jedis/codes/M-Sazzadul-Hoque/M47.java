    @Override
    @SuppressWarnings("unchecked")
    public List<Boolean> build(Object data) {
      if (null == data) return null;
      return ((List<Object>) data).stream()
          //.map((val) -> (val instanceof JedisDataException) ? val : BOOLEAN.build(val))
          .map((val) -> (val instanceof JedisDataException) ? null : BOOLEAN.build(val))
          .collect(Collectors.toList());
    }
