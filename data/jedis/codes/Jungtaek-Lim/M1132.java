  private static byte[] processBulkReply(final RedisInputStream is) {
    final int len = is.readIntCrLf();
    if (len == -1) {
      return null;
    }

    final byte[] read = new byte[len];
    int offset = 0;
    while (offset < len) {
      final int size = is.read(read, offset, (len - offset));
      if (size == -1) {
        throw new JedisConnectionException("It seems like server has closed the connection.");
      }
      offset += size;
    }

    // read 2 more bytes for the command delimiter
    is.readByte();
    is.readByte();

    return read;
  }
