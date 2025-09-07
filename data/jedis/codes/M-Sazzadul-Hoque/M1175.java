  public static Double parseEncodedFloatingPointNumber(Object val) throws NumberFormatException {
    if (val == null) return null;
    else if (val instanceof Double) return (Double) val;
    else return parseFloatingPointNumber((String) val);
  }
