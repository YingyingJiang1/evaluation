    @Override
    public void watch(WatchRequest watchRequest, StreamObserver<ResponseBody> responseObserver){
        // 解析watchRequest 参数
        watchRequestModel = new WatchRequestModel(watchRequest);
        ArthasStreamObserverImpl<ResponseBody> newArthasStreamObserver = new ArthasStreamObserverImpl<>(responseObserver, watchRequestModel, grpcJobController);
        // arthasStreamObserver 传入到advisor中，实现异步传输数据
        if(grpcJobController.containsJob(watchRequestModel.getJobId())){
            arthasStreamObserver = grpcJobController.getGrpcJob(watchRequest.getJobId());
            if(arthasStreamObserver != null && arthasStreamObserver.getPorcessStatus() == ExecStatus.RUNNING){
                WatchRpcAdviceListener listener = (WatchRpcAdviceListener) AdviceWeaver.listener(arthasStreamObserver.getListener().id());
                watchRequestModel.setListenerId(listener.id());
                arthasStreamObserver.setRequestModel(watchRequestModel);
                listener.setArthasStreamObserver(arthasStreamObserver);
                arthasStreamObserver.appendResult(new MessageModel("SUCCESS CHANGE!!!!!!!!!!!"));
                newArthasStreamObserver.setProcessStatus(ExecStatus.RUNNING);
                newArthasStreamObserver.end(0,"修改成功!!!");
                return;
            }else {
                arthasStreamObserver = newArthasStreamObserver;
            }
        }else {
            arthasStreamObserver = newArthasStreamObserver;
        }
        // 创建watch任务
        WatchTask watchTask = new WatchTask();
        // 执行watch任务
        DemoBootstrap.getRunningInstance().execute(watchTask);
    }
