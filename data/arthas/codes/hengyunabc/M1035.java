    @Override
    public void handle(Future<Void> event) {
        if (event.failed()) {
            logger.error("Error listening term server:", event.cause());
            isBindRef.compareAndSet(true, false);
        }
    }
