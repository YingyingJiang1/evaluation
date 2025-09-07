  private byte[] readLineBytesSlowly() {
    ByteArrayOutputStream bout = null;
    while (true) {
      ensureFill();

      byte b = buf[count++];
      if (b == '\r') {
        ensureFill(); // Must be one more byte

        byte c = buf[count++];
        if (c == '\n') {
          break;
        }

        if (bout == null) {
          bout = new ByteArrayOutputStream(16);
        }

        bout.write(b);
        bout.write(c);
      } else {
        if (bout == null) {
          bout = new ByteArrayOutputStream(16);
        }

        bout.write(b);
      }
    }

    return bout == null ? new byte[0] : bout.toByteArray();
  }
