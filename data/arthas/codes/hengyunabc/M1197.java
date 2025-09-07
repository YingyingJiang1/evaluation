            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                try {
                    doneHandler.accept(future.cause());
                } finally {
                    group.shutdownGracefully();
                }
            }
