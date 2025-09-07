  @Override
  protected CommandArguments processKey(String key) {
    final int hashSlot = JedisClusterCRC16.getSlot(key);
    if (commandHashSlot < 0) {
      commandHashSlot = hashSlot;
    } else if (commandHashSlot != hashSlot) {
      throw new JedisClusterOperationException("Keys must belong to same hashslot.");
    }
    return this;
  }
