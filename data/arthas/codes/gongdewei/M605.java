    private ProfilerModel createProfilerModel(String result) {
        ProfilerModel profilerModel = new ProfilerModel();
        profilerModel.setAction(action);
        profilerModel.setActionArg(actionArg);
        profilerModel.setExecuteResult(result);
        return profilerModel;
    }
