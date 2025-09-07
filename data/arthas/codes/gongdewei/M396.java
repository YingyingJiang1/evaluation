    @Override
    public void appendResult(ResultModel model) {
        ResultView resultView = resultViewResolver.getResultView(model);
        if (resultView != null) {
            resultView.draw(commandProcess, model);
        }
    }
