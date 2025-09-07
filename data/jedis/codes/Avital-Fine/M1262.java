  public Query addParam(String name, Object value) {
    if (_params == null) {
      _params = new HashMap<>();
    }
    _params.put(name, value);
    return this;
  }
