        public void copyTo(ResultConsumer consumer) {
            //复制时加锁，避免消息顺序错乱，这里堵塞只影响分发线程，不会影响到业务线程
            queueLock.lock();
            try {
                for (ResultModel result : resultQueue) {
                    consumer.appendResult(result);
                }
                //发送输入状态
                if (lastInputStatus != null) {
                    consumer.appendResult(lastInputStatus);
                }
            } finally {
                if (queueLock.isHeldByCurrentThread()) {
                    queueLock.unlock();
                }
            }
        }
