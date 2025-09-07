    @Override
    @SuppressWarnings("unchecked")
    public List<Double> build(Object data) {
      if (null == data) return null;
      return ((List<Object>) data).stream().map(DOUBLE::build).collect(Collectors.toList());
    }
