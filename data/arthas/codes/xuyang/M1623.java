    @Override
    public void appendResult(ResultModel result) {
        if (process.status() != ExecStatus.RUNNING) {
            throw new IllegalStateException(
                    "Cannot write to standard output when " + process.status().name().toLowerCase());
        }
        result.setJobId(jobId);
        if (resultDistributor != null) {
            resultDistributor.appendResult(result);
        }
    }
