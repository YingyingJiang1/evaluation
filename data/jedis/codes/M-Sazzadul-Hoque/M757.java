  @Override
  public byte[] aclDryRunBinary(byte[] username, CommandArguments commandArgs) {
    checkIsInMultiOrPipeline();
    CommandArguments allArgs = new CommandArguments(ACL).add(DRYRUN).add(username);
    Iterator<Rawable> it = commandArgs.iterator();
    while (it.hasNext()) allArgs.add(it.next());
    connection.sendCommand(allArgs);
    return connection.getBinaryBulkReply();
  }
