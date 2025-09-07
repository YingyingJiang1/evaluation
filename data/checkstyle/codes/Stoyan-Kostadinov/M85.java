    public static String getLinkToDocument(String moduleName, String document)
            throws MacroExecutionException {
        final Path templatePath = getTemplatePath(moduleName.replace("Check", ""));
        if (templatePath == null) {
            throw new MacroExecutionException(
                    String.format(Locale.ROOT,
                            "Could not find template for %s", moduleName));
        }
        final Path templatePathParent = templatePath.getParent();
        if (templatePathParent == null) {
            throw new MacroExecutionException("Failed to get parent path for " + templatePath);
        }
        return templatePathParent
                .relativize(Path.of(SRC, "site/xdoc", document))
                .toString()
                .replace(".xml", ".html")
                .replace('\\', '/');
    }
