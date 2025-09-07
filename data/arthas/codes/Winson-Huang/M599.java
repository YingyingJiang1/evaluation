    @Option(longName = "chunktime")
    @Description("approximate time limits for a single JFR chunk in second (default: 1 hour) or other units")
    public void setChunktime(String chunktime) {
        this.chunktime = chunktime;
    }
