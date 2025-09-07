  public CommandArguments addObjects(Object... args) {
    for (Object arg : args) {
      add(arg);
    }
    return this;
  }
