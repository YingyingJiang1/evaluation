    private synchronized void setEvictTimer() {
        if (!closed && reaperInterval > 0) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    final Thread t = new Thread(r, "arthas-session-manager");
                    t.setDaemon(true);
                    return t;
                }
            });
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    evictSessions();
                }
            }, 0, reaperInterval, TimeUnit.MILLISECONDS);
        }
    }
