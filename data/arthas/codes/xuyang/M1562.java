    @Override
    public void draw(ArthasStreamObserver arthasStreamObserver, PwdModel result) {
        StringStringMapValue stringStringMapValue = StringStringMapValue.newBuilder()
                .putStringStringMap("workingDir", result.getWorkingDir()).build();
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(result.getJobId())
                .setType(result.getType())
                .setStringStringMapValue(stringStringMapValue)
                .build();
        arthasStreamObserver.onNext(responseBody);
    }
