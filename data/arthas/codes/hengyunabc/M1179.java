    @Override
    public Term stdinHandler(final Handler<String> handler) {
        if (inReadline) {
            throw new IllegalStateException();
        }
        stdinHandler = handler;
        if (handler != null) {
            conn.setStdinHandler(new StdinHandlerWrapper(handler));
            checkPending();
        } else {
            conn.setStdinHandler(echoHandler);
        }
        return this;
    }
