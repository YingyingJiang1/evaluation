    @Override
    public synchronized void loadHistory() {
        try {
            history = FileUtils.loadCommandHistoryString(new File(Constants.CMD_HISTORY_FILE));
        } catch (Throwable e) {
            logger.error("load command history failed", e);
        }
    }
