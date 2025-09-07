  private void processPingReply(Object reply) {
    byte[] resp = (byte[]) reply;
    if ("PONG".equals(SafeEncoder.encode(resp))) {
      onPong(null);
    } else {
      onPong(encode(resp));
    }
  }
