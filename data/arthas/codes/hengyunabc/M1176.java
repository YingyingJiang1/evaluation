    @Override
    public Term closeHandler(final Handler<Void> handler) {
        if (handler != null) {
            conn.setCloseHandler(new CloseHandlerWrapper(handler));
        } else {
            conn.setCloseHandler(null);
        }
        return this;
    }
