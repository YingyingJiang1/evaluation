    @Option(longName = "lockedSynchronizers", flag = true)
    @Description("Find the thread info with lockedSynchronizers flag, default value is false.")
    public void setLockedSynchronizers(boolean lockedSynchronizers) {
        this.lockedSynchronizers = lockedSynchronizers;
    }
