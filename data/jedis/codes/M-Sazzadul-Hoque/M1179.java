  @Experimental
  public boolean peek(byte b) throws JedisConnectionException {
    ensureFill(); // in current design, at least one reply is expected. so ensureFillSafe() is not necessary.
    return buf[count] == b;
  }
