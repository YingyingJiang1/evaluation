    @Override
    public void appendResult(ResultModel result) {
        if (!resultQueue.offer(result)) {
            logger.warn("result queue is full: {}, discard later result: {}", resultQueue.size(), JSON.toJSONString(result));
        }
    }
