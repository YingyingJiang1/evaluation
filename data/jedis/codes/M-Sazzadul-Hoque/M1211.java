  @Override
  public Connection getConnection(CommandArguments args) {
    final Long hash = ((ShardedCommandArguments) args).getKeyHash();
    return hash != null ? getConnection(getNodeFromHash(hash)) : getConnection();
  }
