    @Override
    public ArthasStreamObserver write(String msg) {
        ResponseBody result = ResponseBody.newBuilder().setStringValue(msg).build();
        onNext((T) result);
        return this;
    }
