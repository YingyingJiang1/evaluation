    @Override
    public void run() {

      running.set(true);

      while (running.get()) {

        try {
          // double check that it is not being shutdown
          if (!running.get()) {
            break;
          }

          sentinelJedis = new Jedis(node, sentinelClientConfig);

          // code for active refresh
          List<String> masterAddr = sentinelJedis.sentinelGetMasterAddrByName(masterName);
          if (masterAddr == null || masterAddr.size() != 2) {
            LOG.warn("Can not get master {} address. Sentinel: {}.", masterName, node);
          } else {
            initMaster(toHostAndPort(masterAddr));
          }

          sentinelJedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
              LOG.debug("Sentinel {} published: {}.", node, message);

              String[] switchMasterMsg = message.split(" ");

              if (switchMasterMsg.length > 3) {

                if (masterName.equals(switchMasterMsg[0])) {
                  initMaster(toHostAndPort(switchMasterMsg[3], switchMasterMsg[4]));
                } else {
                  LOG.debug(
                      "Ignoring message on +switch-master for master {}. Our master is {}.",
                      switchMasterMsg[0], masterName);
                }

              } else {
                LOG.error("Invalid message received on sentinel {} on channel +switch-master: {}.",
                    node, message);
              }
            }
          }, "+switch-master");

        } catch (JedisException e) {

          if (running.get()) {
            LOG.error("Lost connection to sentinel {}. Sleeping {}ms and retrying.", node,
                subscribeRetryWaitTimeMillis, e);
            try {
              Thread.sleep(subscribeRetryWaitTimeMillis);
            } catch (InterruptedException se) {
              LOG.error("Sleep interrupted.", se);
            }
          } else {
            LOG.debug("Unsubscribing from sentinel {}.", node);
          }
        } finally {
          IOUtils.closeQuietly(sentinelJedis);
        }
      }
    }
