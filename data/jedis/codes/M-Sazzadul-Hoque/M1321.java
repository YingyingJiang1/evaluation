  public long getLong(String key) {
    if (!containsKey(key)) {
      return 0;
    }
    return Long.parseLong((String) fields.get(key));
  }
