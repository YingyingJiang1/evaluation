  @Override
  public StreamFullInfo xinfoStreamFull(String key) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xinfoStreamFull(key));
  }
