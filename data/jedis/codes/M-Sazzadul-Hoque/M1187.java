  private static Object prefixKey(Object key, String prefixString, byte[] prefixBytes) {
    if (key instanceof Rawable) {
      byte[] raw = ((Rawable) key).getRaw();
      return RawableFactory.from(prefixKeyWithBytes(raw, prefixBytes));
    } else if (key instanceof byte[]) {
      return prefixKeyWithBytes((byte[]) key, prefixBytes);
    } else if (key instanceof String) {
      String raw = (String) key;
      return prefixString + raw;
    }
    throw new IllegalArgumentException("\"" + key.toString() + "\" is not a valid argument.");
  }
