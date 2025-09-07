  private void ensureCrLf() {
    final byte[] buf = this.buf;

    ensureFill();
    if (buf[count++] == '\r') {

      ensureFill();
      if (buf[count++] == '\n') {
        return;
      }
    }

    throw new JedisConnectionException("Unexpected character!");
  }
