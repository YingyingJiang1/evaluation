    @Option(shortName = "c", longName = "classLoader")
    @Description("The hash code of the special class's classLoader, default classLoader is SystemClassLoader.")
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
