                @Override
                public void operationComplete(ChannelFuture future) {
                    if (!future.isSuccess()) {
                        // 写入操作失败
                        isSuccessSendData = false;
                    }
                }
