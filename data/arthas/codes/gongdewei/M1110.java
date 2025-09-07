    @Override
    public synchronized void addHistory(String commandLine) {
        while (history.size() >= MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(commandLine);
    }
