  public String getString(String key) {
    if (!containsKey(key)) {
      return "";
    }
    return (String) fields.get(key);
  }
