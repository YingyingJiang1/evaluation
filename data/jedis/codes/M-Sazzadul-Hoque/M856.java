  private static byte[][] joinParameters(byte[] first, byte[] second, byte[][] rest) {
    byte[][] result = new byte[rest.length + 2][];
    result[0] = first;
    result[1] = second;
    System.arraycopy(rest, 0, result, 2, rest.length);
    return result;
  }
