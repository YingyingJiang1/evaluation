    @Option(longName = "state")
    @Description("Query recordings by sate (new, delay, running, stopped, closed)")
    public void setState(String state) {
        this.state = state;
    }
