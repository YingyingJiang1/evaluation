    @Option(longName = "maxsize")
    @Description("Maximum amount of bytes to keep (on disk) in (k)B, (M)B or (G)B, e.g. 500M, 0 for no limit (MEMORY SIZE, 250MB)")
    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }
