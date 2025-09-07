  @Override
  public Response<Long> rpush(String key, String... string) {
    return appendCommand(commandObjects.rpush(key, string));

  }
