    @Override
    public void appendResult(ResultModel result) {
        for (ResultDistributor distributor : distributors) {
            distributor.appendResult(result);
        }
    }
