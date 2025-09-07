    @Override
    public boolean interrupt(final Handler<Void> completionHandler) {
        if (processStatus == ExecStatus.RUNNING || processStatus == ExecStatus.STOPPED) {
            final Handler<Void> handler = interruptHandler;
            try {
                if (handler != null) {
                    handler.handle(null);
                }
            } finally {
                if (completionHandler != null) {
                    completionHandler.handle(null);
                }
            }
            return handler != null;
        } else {
            throw new IllegalStateException("Cannot interrupt process in " + processStatus + " state");
        }
    }
