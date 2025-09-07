    private boolean shouldFlush(List<ResultModel> sendingResults, ResultModel last) {
        //TODO 引入一个估算模型，每个model自统计对象数量
        sendingItemCount += ResultConsumerHelper.getItemCount(last);
        return sendingItemCount >= 100;
    }
