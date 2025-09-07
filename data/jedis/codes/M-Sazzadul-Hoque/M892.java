  public static AggregationType safeValueOf(String str) {
    try {
      return AggregationType.valueOf(str.replace('.', '_').toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException iae) {
      return null;
    }
  }
