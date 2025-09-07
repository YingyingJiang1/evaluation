  private void ensureFill() throws JedisConnectionException {
    if (count >= limit) {
      try {
        limit = in.read(buf);
        count = 0;
        if (limit == -1) {
          throw new JedisConnectionException("Unexpected end of stream.");
        }
      } catch (IOException e) {
        throw new JedisConnectionException(e);
      }
    }
  }
