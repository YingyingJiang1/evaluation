    @Override
    public void appendResult(ResultModel model) {
        GrpcResultView resultView = grpcResultViewResolver.getResultView(model);
        if (resultView != null) {
            resultView.draw(arthasStreamObserver, model);
        }
    }
