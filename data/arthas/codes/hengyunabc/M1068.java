    private synchronized boolean terminate(int exitCode, Handler<Void> completionHandler, String message) {
        if (processStatus != ExecStatus.TERMINATED) {
            //add status message
            this.appendResult(new StatusModel(exitCode, message));
            if (process != null) {
                processOutput.close();
            }
            updateStatus(ExecStatus.TERMINATED, exitCode, false, endHandler, terminatedHandler, completionHandler);
            if (process != null) {
                process.unregister();
            }
            return true;
        } else {
            return false;
        }
    }
