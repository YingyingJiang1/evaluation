    @Option(shortName = "i", longName = "interval")
    @Description("sampling interval in ns (default: 10'000'000, i.e. 10 ms)")
    @DefaultValue("10000000")
    public void setInterval(long interval) {
        this.interval = interval;
    }
