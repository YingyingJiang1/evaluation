    @Override
    @SuppressWarnings("unchecked")
    public List<List<String>> build(Object data) {
      if (null == data) return null;
      return ((List<Object>) data).stream().map(STRING_LIST::build).collect(Collectors.toList());
    }
