  @Override
  public List<String> aclCat(String category) {
    checkIsInMultiOrPipeline();
    connection.sendCommand(ACL, CAT.name(), category);
    return BuilderFactory.STRING_LIST.build(connection.getOne());
  }
