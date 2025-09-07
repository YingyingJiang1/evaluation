            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                try {
                    doneHandler.accept(future.cause());
                } finally {
                    group.shutdownGracefully();
                }
            }
