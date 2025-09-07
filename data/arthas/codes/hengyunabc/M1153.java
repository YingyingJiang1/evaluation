    public void stop(final Consumer<Throwable> doneHandler) {
        if (channel != null) {
            channel.close();
        }

        channelGroup.close().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                try {
                    doneHandler.accept(future.cause());
                } finally {
                    group.shutdownGracefully();
                }
            }
        });
    }
