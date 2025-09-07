    @Override
    public synchronized void saveHistory() {
        try {
            FileUtils.saveCommandHistoryString(history, new File(Constants.CMD_HISTORY_FILE));
        } catch (Throwable e) {
            logger.error("save command history failed", e);
        }
    }
