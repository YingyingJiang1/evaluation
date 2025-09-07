            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                if (total < 0) { // total unknown
                    logger.info(future.channel() + " Transfer progress: " + progress);
                } else {
                    logger.info(future.channel() + " Transfer progress: " + progress + " / " + total);
                }
            }
