    @Option(longName = "chunksize")
    @Description("approximate size limits for a single JFR chunk in bytes (default: 100 MB) or other units")
    public void setChunksize(String chunksize) {
        this.chunksize = chunksize;
    }
