    @Option(shortName = "x", longName = "expand")
    @Description("Expand level of object (1 by default), the max value is " + ObjectView.MAX_DEEP)
    public void setExpand(Integer expand) {
        this.expand = expand;
    }
