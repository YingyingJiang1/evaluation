    @Override
    public void handle(Future<Void> event) {
        if (count.decrementAndGet() == 0) {
            completionHandler.handle(Future.<Void>succeededFuture());
        }
    }
