  @Override
  public final CommandObject<ScanResult<String>> scan(String cursor, ScanParams params, String type) {
    String match = params.match();
    if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
      throw new IllegalArgumentException(SCAN_PATTERN_MESSAGE);
    }
    return new CommandObject<>(commandArguments(SCAN).add(cursor).addParams(params).processKey(match).add(TYPE).add(type), BuilderFactory.SCAN_RESPONSE);
  }
