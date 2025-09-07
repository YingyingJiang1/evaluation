    @Option(longName = "ttsp", flag = true)
    @Description("time-to-safepoint profiling. "
        + "An alias for --begin SafepointSynchronize::begin --end RuntimeService::record_safepoint_synchronized")
    public void setTtsp(boolean ttsp) {
        this.ttsp = ttsp;
    }
