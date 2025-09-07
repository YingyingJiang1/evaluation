  private static byte[][] joinParameters(int... params) {
    byte[][] result = new byte[params.length][];
    for (int i = 0; i < params.length; i++) {
      result[i] = toByteArray(params[i]);
    }
    return result;
  }
