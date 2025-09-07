    public CompletableFuture<Void> stop() {
        CompletableFuture<Void> fut = new CompletableFuture<Void>();
        stop(Helper.stoppedHandler(fut));
        return fut;
    }
