    private void shutdownWorkGroup() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully(200, 200, TimeUnit.MILLISECONDS);
            workerGroup = null;
        }
    }
