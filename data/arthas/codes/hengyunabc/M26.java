    @Option(longName = "session-timeout")
    @Description("The session timeout seconds, default 1800 (30min)")
    public void setSessionTimeout(Long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
