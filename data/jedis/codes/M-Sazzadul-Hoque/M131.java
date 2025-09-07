  @Override
  @Deprecated
  public Response<Long> zdiffStore(byte[] dstkey, byte[]... keys) {
    return appendCommand(commandObjects.zdiffStore(dstkey, keys));
  }
