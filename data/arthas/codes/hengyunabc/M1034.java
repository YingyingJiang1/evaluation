    @Override
    public boolean deliver(int key) {
        if (shell.getForegroundJob() != null) {
            return shell.getForegroundJob().interrupt();
        }
        return true;
    }
