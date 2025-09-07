  @Override
  public StreamFullInfo xinfoStreamFull(String key, int count) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.xinfoStreamFull(key, count));
  }
