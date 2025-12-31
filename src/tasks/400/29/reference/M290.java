    public String getStatus(String name, long timeout) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] data = null;
        long endTime = Time.currentElapsedTime() + timeout;
        KeeperException lastException = null;
        for(int i = 0; i < maxTries && endTime > Time.currentElapsedTime(); i++) {
            try {
                data = zk.getData(reportsNode + '/' + name, false, stat);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Got Data: " + ((data == null) ? "null" : new String(data)));
                }
                lastException = null;
                break;
            } catch(ConnectionLossException e) {
                lastException = e;
            } catch(NoNodeException e) {
                final Object eventObj = new Object();
                synchronized(eventObj) {
                    // wait for the node to appear
                    Stat eStat = zk.exists(reportsNode + '/' + name, new Watcher() {
                        public void process(WatchedEvent event) {
                            synchronized(eventObj) {
                                eventObj.notifyAll();
                            }
                        }});
                    if (eStat == null) {
                        eventObj.wait(endTime - Time.currentElapsedTime());
                    }
                }
                lastException = e;
            }
        }
        if (lastException != null) {
            throw lastException;
        }
        return new String(data);
    }
