    @Option(shortName = "l", longName = "list-classloader", flag = true)
    @Description("Display statistics info by classloader instance")
    public void setListClassLoader(boolean listClassLoader) {
        this.listClassLoader = listClassLoader;
    }
