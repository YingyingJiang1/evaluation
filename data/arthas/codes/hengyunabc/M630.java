    @Option(shortName = "t", longName = "time-tunnel", flag = true)
    @Description("Record the method invocation within time fragments")
    public void setTimeTunnel(boolean timeTunnel) {
        isTimeTunnel = timeTunnel;
    }
