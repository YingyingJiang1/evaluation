    @Option(shortName = "e", longName = "event")
    @Description("which event to trace (cpu, alloc, lock, cache-misses etc.), default value is cpu")
    @DefaultValue("cpu")
    public void setEvent(String event) {
        this.event = event;
    }
