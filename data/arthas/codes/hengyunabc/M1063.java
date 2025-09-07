    @Override
    public synchronized void resume(boolean fg, Handler<Void> completionHandler) {
        if (processStatus == ExecStatus.STOPPED) {
            updateStatus(ExecStatus.RUNNING, null, fg, resumeHandler, terminatedHandler, completionHandler);
            if (process != null) {
                process.resume();
            }
        } else {
            throw new IllegalStateException("Cannot resume process in " + processStatus + " state");
        }
    }
