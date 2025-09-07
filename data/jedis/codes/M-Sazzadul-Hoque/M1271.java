  public String getString(String key) {
    Object value = fields.get(key);
    if (value == null) {
      return null;
    } else if (value instanceof String) {
      return (String) value;
    } else if (value instanceof byte[]) {
      return SafeEncoder.encode((byte[]) value);
    } else {
      return String.valueOf(value);
    }
  }
