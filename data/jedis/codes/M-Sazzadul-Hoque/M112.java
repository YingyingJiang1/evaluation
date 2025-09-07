    @Override
    @SuppressWarnings("unchecked")
    public List<List<Object>> build(Object data) {
      if (null == data) return null;
      return ((List<Object>) data).stream().map(ENCODED_OBJECT_LIST::build).collect(Collectors.toList());
    }
