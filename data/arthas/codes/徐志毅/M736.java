    @Option(shortName = "E", longName = "regex", flag = true)
    @Description("Enable regular expression to match attribute name (wildcard matching by default).")
    public void setRegEx(boolean regEx) {
        isRegEx = regEx;
    }
