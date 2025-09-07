    @Override
    @SuppressWarnings("unchecked")
    public List<Tuple> build(Object data) {
      if (data == null) return null;
      return ((List<List<Object>>) data).stream().map(TUPLE::build).collect(Collectors.toList());
    }
