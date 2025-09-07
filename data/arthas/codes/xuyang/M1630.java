    @Override
    protected AdviceListener getAdviceListener(ArthasStreamObserver arthasStreamObserver) {
        WatchRequestModel watchRequestModel = (WatchRequestModel) arthasStreamObserver.getRequestModel();
        if (watchRequestModel.getListenerId()!= 0) {
            AdviceListener listener = AdviceWeaver.listener(watchRequestModel.getListenerId());
            if (listener != null) {
                return listener;
            }
        }
        return new WatchRpcAdviceListener(arthasStreamObserver, GlobalOptions.verbose || watchRequestModel.isVerbose());
    }
