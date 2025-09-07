    @Override
    public void get(Empty empty, StreamObserver<ResponseBody> responseObserver){
        ArthasStreamObserver<ResponseBody> arthasStreamObserver = new ArthasStreamObserverImpl<>(responseObserver, null, grpcJobController);
        arthasStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        arthasStreamObserver.appendResult(new SystemPropertyModel(System.getProperties()));
        arthasStreamObserver.end();
    }
