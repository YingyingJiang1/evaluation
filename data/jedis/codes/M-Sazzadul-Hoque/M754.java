  @Override
  public String aclDryRun(String username, String command, String... args) {
    checkIsInMultiOrPipeline();
    String[] allArgs = new String[3 + args.length];
    allArgs[0] = DRYRUN.name();
    allArgs[1] = username;
    allArgs[2] = command;
    System.arraycopy(args, 0, allArgs, 3, args.length);
    connection.sendCommand(ACL, allArgs);
    return connection.getBulkReply();
  }
