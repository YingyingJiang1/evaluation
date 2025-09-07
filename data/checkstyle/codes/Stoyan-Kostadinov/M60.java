    public static Path getTemplatePath(String moduleName) throws MacroExecutionException {
        final String fileNamePattern = ".*[\\\\/]"
                + moduleName.toLowerCase(Locale.ROOT) + "\\..*";
        return getXdocsTemplatesFilePaths()
            .stream()
            .filter(path -> path.toString().matches(fileNamePattern))
            .findFirst()
            .orElse(null);
    }
