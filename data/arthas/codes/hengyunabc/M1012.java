    @Override
    public CommandBuilderImpl completionHandler(Handler<Completion> handler) {
        completeHandler = handler;
        return this;
    }
