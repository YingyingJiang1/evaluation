    @Option(shortName = "v", longName = "invert-match", flag = true)
    @Description("Select non-matching lines")
    public void setInvertMatch(boolean invertMatch) {
        this.invertMatch = invertMatch;
    }
