  public static int hash(ByteBuffer buf, int seed) {
    // save byte order for later restoration
    ByteOrder byteOrder = buf.order();
    buf.order(ByteOrder.LITTLE_ENDIAN);

    int m = 0x5bd1e995;
    int r = 24;

    int h = seed ^ buf.remaining();

    int k;
    while (buf.remaining() >= 4) {
      k = buf.getInt();

      k *= m;
      k ^= k >>> r;
      k *= m;

      h *= m;
      h ^= k;
    }

    if (buf.remaining() > 0) {
      ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
      // for big-endian version, use this first:
      // finish.position(4-buf.remaining());
      finish.put(buf).rewind();
      h ^= finish.getInt();
      h *= m;
    }

    h ^= h >>> 13;
    h *= m;
    h ^= h >>> 15;

    buf.order(byteOrder);
    return h;
  }
