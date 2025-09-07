    @Override
    public void stop(final Consumer<Throwable> doneHandler) {
        GenericFutureListener<Future<Object>> adapter = new GenericFutureListener<Future<Object>>() {
            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                try {
                    doneHandler.accept(future.cause());
                } finally {
                    group.shutdownGracefully();
                }
            }
        };
        channelGroup.close().addListener(adapter);
    }
