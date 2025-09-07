  @Override
  public List<String> hgetdel(String key, String... fields) {    
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.hgetdel(key, fields));
  }
