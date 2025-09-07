    @Override
    public synchronized int resetAll() {
        int result = this.accessTimes.size();
        accessTimes.clear();
        return result;
    }
