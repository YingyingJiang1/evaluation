    @Option(shortName = "l", longName = "live", flag = true)
    @Description("Dump only live objects; if not specified, all objects in the heap are dumped.")
    public void setLive(boolean live) {
        this.live = live;
    }
