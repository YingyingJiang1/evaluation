    @Override
    public void toForeground(Handler<Void> completionHandler) {
        if (processStatus == ExecStatus.RUNNING) {
            if (!processForeground) {
                updateStatus(ExecStatus.RUNNING, null, true, foregroundHandler, terminatedHandler, completionHandler);
            }
        } else {
            throw new IllegalStateException("Cannot set to foreground a process in " + processStatus + " state");
        }
    }
