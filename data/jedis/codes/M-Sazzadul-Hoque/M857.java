  private static String[] joinParameters(String first, String[] rest) {
    String[] result = new String[rest.length + 1];
    result[0] = first;
    System.arraycopy(rest, 0, result, 1, rest.length);
    return result;
  }
