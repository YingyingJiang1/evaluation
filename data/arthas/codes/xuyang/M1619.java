    @Override
    public void onCompleted() {
        this.process.setProcessStatus(ExecStatus.TERMINATED);
//        grpcJobController.unRegisterGrpcJob(this.jobId);
        streamObserver.onCompleted();
    }
