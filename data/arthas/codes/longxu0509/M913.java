    @Option(longName = "dumponexit")
    @Description("Dump running recording when JVM shuts down (BOOLEAN, false)")
    public void setDumpOnExit(Boolean dumpOnExit) {
        this.dumpOnExit = dumpOnExit;
    }
