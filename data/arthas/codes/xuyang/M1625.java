    private synchronized boolean terminate(int exitCode, String message) {
        boolean flag;
        if (process.status() != ExecStatus.TERMINATED) {
            //add status message
            this.appendResult(new StatusModel(exitCode, message));
            if (process != null) {
                this.unregister();
            }
            flag = true;
        } else {
            flag = false;
        }
        this.onCompleted();
        return flag;
    }
