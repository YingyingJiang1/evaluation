  public final CommandObject<String> cmsMerge(String destKey, Map<String, Long> keysAndWeights) {
    CommandArguments args = commandArguments(CountMinSketchCommand.MERGE).key(destKey);
    args.add(keysAndWeights.size());
    keysAndWeights.entrySet().forEach(entry -> args.key(entry.getKey()));
    args.add(RedisBloomKeyword.WEIGHTS);
    keysAndWeights.entrySet().forEach(entry -> args.add(entry.getValue()));
    return new CommandObject<>(args, BuilderFactory.STRING);
  }
