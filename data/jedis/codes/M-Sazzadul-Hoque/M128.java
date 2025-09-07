  @Deprecated
  @Override
  public Response<String> getSet(String key, String value) {
    return appendCommand(commandObjects.getSet(key, value));
  }
