    @Override
    public void toBackground(Handler<Void> completionHandler) {
        if (processStatus == ExecStatus.RUNNING) {
            if (processForeground) {
                updateStatus(ExecStatus.RUNNING, null, false, backgroundHandler, terminatedHandler, completionHandler);
            }
        } else {
            throw new IllegalStateException("Cannot set to background a process in " + processStatus + " state");
        }
    }
