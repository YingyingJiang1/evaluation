  public void write(final byte b) throws IOException {
    if (count == buf.length) {
      flushBuffer();
    }
    buf[count++] = b;
  }
