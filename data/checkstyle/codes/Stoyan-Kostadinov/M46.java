    private static String getLinkToParentModule(String parentModule, String moduleName)
            throws MacroExecutionException {
        final Path templatePath = SiteUtil.getTemplatePath(moduleName);
        if (templatePath == null) {
            throw new MacroExecutionException(
                    String.format(Locale.ROOT, "Could not find template for %s", moduleName));
        }
        final Path templatePathParent = templatePath.getParent();
        if (templatePathParent == null) {
            throw new MacroExecutionException("Failed to get parent path for " + templatePath);
        }
        return templatePathParent
                .relativize(Path.of("src", "site/xdoc", "config.xml"))
                .toString()
                .replace(".xml", ".html")
                .replace('\\', '/')
                + "#" + parentModule;
    }
