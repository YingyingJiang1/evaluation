    @Override
    @SuppressWarnings("unchecked")
    public List<Tuple> build(Object data) {
      if (null == data) return null;
      return ((List<Object>) data).stream().map(TUPLE::build).collect(Collectors.toList());
    }
