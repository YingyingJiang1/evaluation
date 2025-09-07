  @Override
  public List<byte[]> hgetex(byte[] key, HGetExParams params, byte[]... fields){
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hgetex(key, params, fields));
  }
