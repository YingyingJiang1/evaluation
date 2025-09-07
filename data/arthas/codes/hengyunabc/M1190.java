    public CompletableFuture<?> start(Consumer<TtyConnection> factory) {
        CompletableFuture<?> fut = new CompletableFuture();
        start(factory, Helper.startedHandler(fut));
        return fut;
    }
