    @Override
    public void handle(E event) {
        if (event instanceof Future && ((Future) event).failed()) {
            logger.error("Error listening term server:", ((Future) event).cause());
        }
    }
