    @Option(longName = "timeout")
    @Description("automatically stop profiler at TIME (absolute or relative)")
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
