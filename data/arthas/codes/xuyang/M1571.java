    @Override
    public void draw(ArthasStreamObserver arthasStreamObserver, StatusModel result) {
        if (result.getMessage() != null) {
            ResponseBody responseBody  = ResponseBody.newBuilder()
                    .setJobId(result.getJobId())
                    .setType(result.getType())
                    .setStringValue(result.getMessage())
                    .build();
            arthasStreamObserver.onNext(responseBody);
        }
    }
