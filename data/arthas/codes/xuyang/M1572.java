    @Override
    public void draw(ArthasStreamObserver arthasStreamObserver, EnhancerModel result) {
        if (result.getEffect() != null) {
            String msg = ViewRenderUtil.renderEnhancerAffect(result.getEffect());
            ResponseBody responseBody  = ResponseBody.newBuilder()
                    .setJobId(result.getJobId())
                    .setType(result.getType())
                    .setStringValue(msg)
                    .build();
            arthasStreamObserver.onNext(responseBody);
        }
    }
