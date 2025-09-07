  public final CommandObject<Long> georadiusByMemberStore(byte[] key, byte[] member,
      double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
    return new CommandObject<>(commandArguments(GEORADIUSBYMEMBER).key(key).add(member)
        .add(radius).add(unit).addParams(param).addParams(storeParam), BuilderFactory.LONG);
  }
