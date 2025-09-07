    @Override
    public void pwd(Empty empty, StreamObserver<ResponseBody> responseObserver){
        String path = new File("").getAbsolutePath();
        ArthasStreamObserver<ResponseBody> arthasStreamObserver = new ArthasStreamObserverImpl<>(responseObserver, null,grpcJobController);
        arthasStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        arthasStreamObserver.appendResult(new PwdModel(path));
        arthasStreamObserver.onCompleted();
    }
