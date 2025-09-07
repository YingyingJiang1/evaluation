  @Deprecated
  @Override
  public Response<byte[]> getSet(byte[] key, byte[] value) {
    return appendCommand(commandObjects.getSet(key, value));
  }
