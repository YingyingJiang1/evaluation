  @Override
  public List<Object> role() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ROLE);
    return BuilderFactory.ENCODED_OBJECT_LIST.build(connection.getOne());
  }
