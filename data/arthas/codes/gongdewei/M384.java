    @Override
    public void appendResult(ResultModel result) {
        //要避免阻塞影响业务线程执行
        try {
            if (!pendingResultQueue.offer(result, 100, TimeUnit.MILLISECONDS)) {
                ResultModel discardResult = pendingResultQueue.poll();
                // 正常情况走不到这里，除非distribute 循环堵塞或异常终止
                // 输出队列满，终止当前执行的命令
                interruptJob("result queue is full: "+ pendingResultQueue.size());
            }
        } catch (InterruptedException e) {
            //ignore
        }
    }
