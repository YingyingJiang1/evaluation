    public void start(final Consumer<TtyConnection> factory, Consumer<Throwable> doneHandler) {
        httpTelnetTtyBootstrap.start(new Supplier<TelnetHandler>() {
            @Override
            public TelnetHandler get() {
                return new TelnetTtyConnection(inBinary, outBinary, charset, factory);
            }
        }, factory, doneHandler);
    }
