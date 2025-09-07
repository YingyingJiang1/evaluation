  protected final CommandArguments processKeys(byte[]... keys) {
    for (byte[] key : keys) {
      processKey(key);
    }
    return this;
  }
