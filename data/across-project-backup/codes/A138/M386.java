    private void distribute() {
        while (running) {
            try {
                ResultModel result = pendingResultQueue.poll(100, TimeUnit.MILLISECONDS);
                if (result != null) {
                    sharingResultConsumer.appendResult(result);
                    //判断是否有至少一个consumer是健康的
                    int healthCount = 0;
                    for (int i = 0; i < consumers.size(); i++) {
                        ResultConsumer consumer = consumers.get(i);
                        if(consumer.isHealthy()){
                            healthCount += 1;
                        }
                        consumer.appendResult(result);
                    }
                    //所有consumer都不是健康状态，终止当前执行的命令
                    if (healthCount == 0) {
                        interruptJob("all consumers are unhealthy");
                    }
                }
            } catch (Throwable e) {
                logger.warn("distribute result failed: " + e.getMessage(), e);
            }
        }
    }
