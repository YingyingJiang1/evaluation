    @Option(shortName = "A", longName = "after-context")
    @Description("Print NUM lines of trailing context)")
    public void setAfterLines(int afterLines) {
        this.afterLines = afterLines;
    }
