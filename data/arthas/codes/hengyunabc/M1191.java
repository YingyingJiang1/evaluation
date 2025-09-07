    public CompletableFuture<?> stop() {
        CompletableFuture<?> fut = new CompletableFuture();
        stop(Helper.stoppedHandler(fut));
        return fut;
    }
