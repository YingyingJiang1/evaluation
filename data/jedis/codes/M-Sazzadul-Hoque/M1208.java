  private void initialize(List<HostAndPort> shards) {
    for (int i = 0; i < shards.size(); i++) {
      HostAndPort shard = shards.get(i);
      for (int n = 0; n < 160; n++) {
        Long hash = this.algo.hash("SHARD-" + i + "-NODE-" + n);
        nodes.put(hash, shard);
        setupNodeIfNotExist(shard);
      }
    }
  }
