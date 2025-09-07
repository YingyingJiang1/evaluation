  @Override
  public Jedis getResource() {
    while (true) {
      Jedis jedis = super.getResource();
      jedis.setDataSource(this);

      // get a reference because it can change concurrently
      final HostAndPort master = currentHostMaster;
      final HostAndPort connection = jedis.getClient().getHostAndPort();

      if (master.equals(connection)) {
        // connected to the correct master
        return jedis;
      } else {
        returnBrokenResource(jedis);
      }
    }
  }
