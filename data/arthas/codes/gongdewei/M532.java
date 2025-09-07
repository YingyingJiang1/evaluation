    private void drawExecuteResult(CommandProcess process, ProfilerModel model) {
        if (model.getExecuteResult() != null) {
            process.write(model.getExecuteResult());
            if (!model.getExecuteResult().endsWith("\n")) {
                process.write("\n");
            }
        }
    }
