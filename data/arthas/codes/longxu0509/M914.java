    @Option(shortName = "d", longName = "delay")
    @Description("Delay recording start with (s)econds, (m)inutes), (h)ours), or (d)ays, e.g. 5h. (NANOTIME, 0)")
    public void setDelay(String delay) {
        this.delay = delay;
    }
