  @Override
  @Deprecated
  public Response<Long> zdiffStore(String dstKey, String... keys) {
    return appendCommand(commandObjects.zdiffStore(dstKey, keys));
  }
