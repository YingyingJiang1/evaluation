    @Argument(argName = "classfilePaths", index = 0, required = false)
    @Description(".class file paths")
    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
