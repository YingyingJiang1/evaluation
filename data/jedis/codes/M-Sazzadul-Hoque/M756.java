  @Override
  public byte[] aclDryRunBinary(byte[] username, byte[] command, byte[]... args) {
    checkIsInMultiOrPipeline();
    byte[][] allArgs = new byte[3 + args.length][];
    allArgs[0] = DRYRUN.getRaw();
    allArgs[1] = username;
    allArgs[2] = command;
    System.arraycopy(args, 0, allArgs, 3, args.length);
    connection.sendCommand(ACL, allArgs);
    return connection.getBinaryBulkReply();
  }
