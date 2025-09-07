  public final CommandObject<String> migrate(String host, int port, int destinationDB, int timeout,
      MigrateParams params, byte[]... keys) {
    return new CommandObject<>(commandArguments(MIGRATE).add(host).add(port).add(new byte[0])
        .add(destinationDB).add(timeout).addParams(params).add(Keyword.KEYS).keys((Object[]) keys),
        BuilderFactory.STRING);
  }
