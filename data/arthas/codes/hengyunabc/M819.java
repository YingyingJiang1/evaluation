    @Option(shortName = "l", longName = "limit")
    @Description("The limit of dump classes size, default value is 50")
    @DefaultValue("50")
    public void setLimit(int limit) {
        this.limit = limit;
    }
