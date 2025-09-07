    public CompletableFuture<Void> start(Consumer<TtyConnection> handler) {
        CompletableFuture<Void> fut = new CompletableFuture<Void>();
        start(handler, Helper.startedHandler(fut));
        return fut;
    }
