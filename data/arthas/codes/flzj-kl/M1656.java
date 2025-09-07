    @Override
    public Map<String, String> findNativeAgent(String address) {
        if (address == null || "".equals(address)) {
            return null;
        }

        // Wait for connection to be established
        try {
            ZooKeeper zooKeeper = new ZooKeeper(address, SESSION_TIMEOUT, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connectedSemaphore.countDown();
                }
            });
            connectedSemaphore.await();

            // Gets a list of all children of the parent node
            List<String> children = zooKeeper.getChildren(NativeAgentConstants.NATIVE_AGENT_KEY, false);

            // Get the data of the child node
            Map<String, String> res = new ConcurrentHashMap<>(children.size());
            for (String child : children) {
                String childPath = NativeAgentConstants.NATIVE_AGENT_KEY + "/" + child;
                byte[] data = zooKeeper.getData(childPath, false, new Stat());
                String dataStr = new String(data);

                res.put(child, dataStr);
            }

            zooKeeper.close();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
