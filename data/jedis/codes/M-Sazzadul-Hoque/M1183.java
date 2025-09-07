  public boolean readBooleanCrLf() {
    final byte[] buf = this.buf;

    ensureFill();
    final byte b = buf[count++];

    ensureCrLf();
    switch (b) {
      case 't':
        return true;
      case 'f':
        return false;
      default:
        throw new JedisConnectionException("Unexpected character!");
    }
  }
