  public final CommandObject<String> tdigestMerge(TDigestMergeParams mergeParams,
      String destinationKey, String... sourceKeys) {
    return new CommandObject<>(commandArguments(TDigestCommand.MERGE).key(destinationKey)
        .add(sourceKeys.length).keys((Object[]) sourceKeys).addParams(mergeParams), BuilderFactory.STRING);
  }
