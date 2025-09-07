    @Override
    public void terminate(Handler<Void> completionHandler) {
        if (!terminate(-10, completionHandler, null)) {
            throw new IllegalStateException("Cannot terminate terminated process");
        }
    }
