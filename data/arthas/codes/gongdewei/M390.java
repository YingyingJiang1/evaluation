        @Override
        public boolean appendResult(ResultModel result) {
            queueLock.lock();
            try {
                //输入状态不入历史指令队列，复制时在最后发送
                if (result instanceof InputStatusModel) {
                    lastInputStatus = (InputStatusModel) result;
                    return true;
                }
                while (!resultQueue.offer(result)) {
                    ResultModel discardResult = resultQueue.poll();
                }
            } finally {
                if (queueLock.isHeldByCurrentThread()) {
                    queueLock.unlock();
                }
            }
            return true;
        }
