  public String readLine() {
    final StringBuilder sb = new StringBuilder();
    while (true) {
      ensureFill();

      byte b = buf[count++];
      if (b == '\r') {
        ensureFill(); // Must be one more byte

        byte c = buf[count++];
        if (c == '\n') {
          break;
        }
        sb.append((char) b);
        sb.append((char) c);
      } else {
        sb.append((char) b);
      }
    }

    final String reply = sb.toString();
    if (reply.isEmpty()) {
      throw new JedisConnectionException("It seems like server has closed the connection.");
    }

    return reply;
  }
