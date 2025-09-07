    @Override
    public void draw(ArthasStreamObserver arthasStreamObserver, MessageModel result) {
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(result.getJobId())
                .setType(result.getType())
                .setStringValue(result.getMessage())
                .build();
        arthasStreamObserver.onNext(responseBody);
    }
