  public double getDouble(String key) {
    if (!containsKey(key)) {
      return 0;
    }
    return DoublePrecision.parseFloatingPointNumber((String) fields.get(key));
  }
