    @Option(shortName = "n", longName = "username")
    @Description("username, default value 'arthas'")
    @DefaultValue(ArthasConstants.DEFAULT_USERNAME)
    public void setUsername(String username) {
        this.username = username;
    }
