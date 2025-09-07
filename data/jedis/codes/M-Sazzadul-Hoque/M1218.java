  private HostAndPort initSentinels(Set<HostAndPort> sentinels) {

    HostAndPort master = null;
    boolean sentinelAvailable = false;

    LOG.debug("Trying to find master from available sentinels...");

    for (HostAndPort sentinel : sentinels) {

      LOG.debug("Connecting to Sentinel {}...", sentinel);

      try (Jedis jedis = new Jedis(sentinel, sentinelClientConfig)) {

        List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);

        // connected to sentinel...
        sentinelAvailable = true;

        if (masterAddr == null || masterAddr.size() != 2) {
          LOG.warn("Sentinel {} is not monitoring master {}.", sentinel, masterName);
          continue;
        }

        master = toHostAndPort(masterAddr);
        LOG.debug("Redis master reported at {}.", master);
        break;
      } catch (JedisException e) {
        // resolves #1036, it should handle JedisException there's another chance
        // of raising JedisDataException
        LOG.warn("Could not get master address from {}.", sentinel, e);
      }
    }

    if (master == null) {
      if (sentinelAvailable) {
        // can connect to sentinel, but master name seems to not monitored
        throw new JedisException(
            "Can connect to sentinel, but " + masterName + " seems to be not monitored.");
      } else {
        throw new JedisConnectionException(
            "All sentinels down, cannot determine where " + masterName + " is running.");
      }
    }

    LOG.info("Redis master running at {}. Starting sentinel listeners...", master);

    for (HostAndPort sentinel : sentinels) {

      SentinelListener listener = new SentinelListener(sentinel);
      // whether SentinelListener threads are alive or not, process can be stopped
      listener.setDaemon(true);
      sentinelListeners.add(listener);
      listener.start();
    }

    return master;
  }
