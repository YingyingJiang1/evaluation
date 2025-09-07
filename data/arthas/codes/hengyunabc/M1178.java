    @Override
    public TermImpl resizehandler(Handler<Void> handler) {
        if (inReadline) {
            throw new IllegalStateException();
        }
        if (handler != null) {
            conn.setSizeHandler(new SizeHandlerWrapper(handler));
        } else {
            conn.setSizeHandler(null);
        }
        return this;
    }
