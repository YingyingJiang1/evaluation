  @Override
  protected CommandArguments processKey(String key) {
    key = getKeyTag(key);
    final long hash = algo.hash(key);
    if (keyHash == null) {
      keyHash = hash;
    } else if (keyHash != hash) {
      throw new JedisException("Keys must generate same hash.");
    }
    return this;
  }
