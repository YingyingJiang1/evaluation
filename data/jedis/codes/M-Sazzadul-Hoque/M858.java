  private static String[] joinParameters(String first, String second, String[] rest) {
    String[] result = new String[rest.length + 2];
    result[0] = first;
    result[1] = second;
    System.arraycopy(rest, 0, result, 2, rest.length);
    return result;
  }
