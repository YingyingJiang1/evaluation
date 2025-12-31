   synchronized public static ZooKeeper getClient(String contextPath,
           String session, int expireTime) throws IOException {
       final String connectionId = concat(contextPath, session);

       ZooKeeper zk = zkMap.get(connectionId);
       if (zk == null) {

           if (LOG.isInfoEnabled()) {
               LOG.info(String.format("creating new "
                       + "connection for : '%s'", connectionId));
           }
           Endpoint e = contextMap.get(contextPath);
           zk = new ZooKeeper(e.getHostPort(), 30000, new MyWatcher(
                   connectionId));
           
           for (Map.Entry<String, String> p : e.getZooKeeperAuthInfo().entrySet()) {
               zk.addAuthInfo("digest", String.format("%s:%s", p.getKey(),
                       p.getValue()).getBytes());
           }
           
           zkMap.put(connectionId, zk);

           // a session should automatically expire after an amount of time
           if (session != null) {
               zkSessionTimers.put(connectionId, new SessionTimerTask(
                       expireTime, session, contextPath, timer));
           }
       }
       return zk;
   }
