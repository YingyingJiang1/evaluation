    @Override
    public List<ResultModel> getResults() {
        ArrayList<ResultModel> results = new ArrayList<ResultModel>(resultQueue.size());
        resultQueue.drainTo(results);
        return results;
    }
