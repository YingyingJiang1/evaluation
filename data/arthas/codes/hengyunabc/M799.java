    @Option(longName = "load")
    @Description("Use ClassLoader to load class, won't work without -c specified")
    public void setLoadClass(String className) {
        this.loadClass = className;
    }
