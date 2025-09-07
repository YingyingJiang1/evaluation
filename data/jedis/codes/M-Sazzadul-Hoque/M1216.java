  private void initMaster(HostAndPort master) {
    initPoolLock.lock();

    try {
      if (!master.equals(currentMaster)) {
        currentMaster = master;

        ConnectionPool newPool = createNodePool(currentMaster);

        ConnectionPool existingPool = pool;
        pool = newPool;
        LOG.info("Created connection pool to master at {}.", master);
        if (clientSideCache != null) {
          clientSideCache.flush();
        }

        if (existingPool != null) {
          // although we clear the pool, we still have to check the returned object in getResource,
          // this call only clears idle instances, not borrowed instances
          // existingPool.clear(); // necessary??
          existingPool.close();
        }
      }
    } finally {
      initPoolLock.unlock();
    }
  }
