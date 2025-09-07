    @Override
    public synchronized void suspend(Handler<Void> completionHandler) {
        if (processStatus == ExecStatus.RUNNING) {
            updateStatus(ExecStatus.STOPPED, null, false, suspendHandler, terminatedHandler, completionHandler);
            if (process != null) {
                process.suspend();
            }
        } else {
            throw new IllegalStateException("Cannot suspend process in " + processStatus + " state");
        }
    }
