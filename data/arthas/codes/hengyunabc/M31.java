    @Option(longName = "use-http", flag = true)
    @Description("Enforce use http to download, default use https")
    public void setuseHttp(boolean useHttp) {
        this.useHttp = useHttp;
    }
