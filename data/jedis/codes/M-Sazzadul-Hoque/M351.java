  @Override
  public List<Object> roleBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ROLE);
    return BuilderFactory.RAW_OBJECT_LIST.build(connection.getOne());
  }
