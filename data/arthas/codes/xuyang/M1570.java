    @Override
    public void draw(ArthasStreamObserver arthasStreamObserver, SystemPropertyModel result) {
        StringStringMapValue stringStringMapValue = StringStringMapValue.newBuilder()
                .putAllStringStringMap(result.getProps()).build();
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(result.getJobId())
                .setType(result.getType())
                .setStringStringMapValue(stringStringMapValue)
                .build();
        arthasStreamObserver.onNext(responseBody);
    }
