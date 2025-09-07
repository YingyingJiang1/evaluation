  @Override
  public Boolean scriptExists(final byte[] sha1) {
    byte[][] a = new byte[1][];
    a[0] = sha1;
    return scriptExists(a).get(0);
  }
