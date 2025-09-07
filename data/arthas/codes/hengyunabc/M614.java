    @Override
    protected void abortProcess(CommandProcess process, int limit) {
        // Only proceed if this thread is the first one to set the flag to true
        if (processAborted.compareAndSet(false, true)) {
            super.abortProcess(process, limit);
        }
    }
