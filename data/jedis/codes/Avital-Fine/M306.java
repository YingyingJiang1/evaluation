  @Override
  public List<byte[]> sortReadonly(byte[] key, SortingParams sortingParams) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.sortReadonly(key, sortingParams));
  }
