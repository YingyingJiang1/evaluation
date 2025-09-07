            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    doneHandler.accept(null);
                } else {
                    doneHandler.accept(future.cause());
                }
            }
