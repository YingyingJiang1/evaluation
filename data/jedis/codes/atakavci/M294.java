  @Override
  public List<byte[]> hgetdel(byte[] key, byte[]... fields){
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hgetdel(key, fields));
  }
