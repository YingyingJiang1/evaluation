  public byte[] getPrefix(byte[] in, Type type) {
    int len = in.length;
    return new byte[] {
        type.value,
        (byte) ((len >> 24) & 0xff),
        (byte) ((len >> 16) & 0xff),
        (byte) ((len >> 8) & 0xff),
        (byte) ((len >> 0) & 0xff),
    };
  }
