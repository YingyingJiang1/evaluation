  private static byte[] prefixKeyWithBytes(byte[] key, byte[] prefixBytes) {
    byte[] namespaced = new byte[prefixBytes.length + key.length];
    System.arraycopy(prefixBytes, 0, namespaced, 0, prefixBytes.length);
    System.arraycopy(key, 0, namespaced, prefixBytes.length, key.length);
    return namespaced;
  }
