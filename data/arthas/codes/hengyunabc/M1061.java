    @Override
    public Process terminatedHandler(Handler<Integer> handler) {
        terminatedHandler = handler;
        return this;
    }
