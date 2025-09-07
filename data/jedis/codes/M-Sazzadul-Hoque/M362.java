  @Override
  public long linsert(final byte[] key, final ListPosition where, final byte[] pivot,
      final byte[] value) {
    checkIsInMultiOrPipeline();
    return connection.executeCommand(commandObjects.linsert(key, where, pivot, value));
  }
