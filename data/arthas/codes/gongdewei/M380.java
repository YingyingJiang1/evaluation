    @Override
    public boolean appendResult(ResultModel result) {
        //可能某些Consumer已经断开，不会再读取，这里不能堵塞！
        boolean discard = false;
        while (!resultQueue.offer(result)) {
            ResultModel discardResult = resultQueue.poll();
            discard = true;
        }
        return !discard;
    }
