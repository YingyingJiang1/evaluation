  protected final CommandArguments processKeys(String... keys) {
    for (String key : keys) {
      processKey(key);
    }
    return this;
  }
