    @Option(longName = "cstack")
    @Description("how to traverse C stack: fp|dwarf|lbr|no")
    public void setCstack(String cstack) {
        this.cstack = cstack;
    }
