    @Override
    public boolean isHealthy() {

        return isPolling()
                || resultQueue.size() < resultQueueSize
                || System.currentTimeMillis() - lastAccessTime < 1000;
    }
