    public Future<T> setHandler(Handler<Future<T>> handler) {
        this.handler = handler;
        checkCallHandler();
        return this;
    }
