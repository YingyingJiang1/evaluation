    @Option(longName = "trim-end", flag = false)
    @DefaultValue("true")
    @Description("Remove whitespaces at the end of the line, default value true")
    public void setTrimEnd(boolean trimEnd) {
        this.trimEnd = trimEnd;
    }
