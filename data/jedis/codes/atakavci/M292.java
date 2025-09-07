  @Override
  public long hsetex(byte[] key, HSetExParams params, Map<byte[], byte[]> hash){
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hsetex(key, params, hash));
  }
