    @Override
    @SuppressWarnings("unchecked")
    public Set<Tuple> build(Object data) {
      if (null == data) return null;
      return ((List<Object>) data).stream().map(TUPLE::build).collect(Collectors.toCollection(LinkedHashSet::new));
    }
