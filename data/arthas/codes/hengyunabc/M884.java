    @Option(shortName = "B", longName = "before-context")
    @Description("Print NUM lines of leading context)")
    public void setBeforeLines(int beforeLines) {
        this.beforeLines = beforeLines;
    }
