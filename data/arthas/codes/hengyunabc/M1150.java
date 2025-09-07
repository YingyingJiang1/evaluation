                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        channel = f.channel();
                        doneHandler.accept(null);
                    } else {
                        doneHandler.accept(future.cause());
                    }
                }
