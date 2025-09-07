  @Override
  public int read(byte[] b, int off, int len) throws JedisConnectionException {
    ensureFill();

    final int length = Math.min(limit - count, len);
    System.arraycopy(buf, count, b, off, length);
    count += length;
    return length;
  }
