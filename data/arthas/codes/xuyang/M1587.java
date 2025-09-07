    @Override
    public void getByKey(StringKey request, StreamObserver<ResponseBody> responseObserver){
        String propertyName = request.getKey();
        ArthasStreamObserver<ResponseBody> arthasStreamObserver = new ArthasStreamObserverImpl<>(responseObserver,null, grpcJobController);
        arthasStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        // view the specified system property
        String value = System.getProperty(propertyName);
        if (value == null) {
            arthasStreamObserver.end(-1, "There is no property with the key " + propertyName);
            return;
        } else {
            arthasStreamObserver.appendResult(new SystemPropertyModel(propertyName, value));
            arthasStreamObserver.end();
        }
    }
