  @Override
  public List<String> hgetex(String key, HGetExParams params, String... fields) {    
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hgetex(key, params, fields));
  }
