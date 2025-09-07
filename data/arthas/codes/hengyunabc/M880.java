    @Option(shortName = "i", longName = "ignore-case", flag = true)
    @Description("Perform case insensitive matching.  By default, grep is case sensitive.")
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
