    private static void processModule(String moduleName, Path modulePath)
            throws MacroExecutionException {
        if (!Files.isRegularFile(modulePath)) {
            final String message = String.format(Locale.ROOT,
                    "File %s is not a file. Please check the 'modulePath' property.", modulePath);
            throw new MacroExecutionException(message);
        }
        ClassAndPropertiesSettersJavadocScraper.initialize(moduleName);
        final Checker checker = new Checker();
        checker.setModuleClassLoader(Checker.class.getClassLoader());
        final DefaultConfiguration scraperCheckConfig =
                        new DefaultConfiguration(
                                ClassAndPropertiesSettersJavadocScraper.class.getName());
        final DefaultConfiguration defaultConfiguration =
                new DefaultConfiguration("configuration");
        final DefaultConfiguration treeWalkerConfig =
                new DefaultConfiguration(TreeWalker.class.getName());
        defaultConfiguration.addProperty(CHARSET, StandardCharsets.UTF_8.name());
        defaultConfiguration.addChild(treeWalkerConfig);
        treeWalkerConfig.addChild(scraperCheckConfig);
        try {
            checker.configure(defaultConfiguration);
            final List<File> filesToProcess = List.of(modulePath.toFile());
            checker.process(filesToProcess);
            checker.destroy();
        }
        catch (CheckstyleException checkstyleException) {
            final String message = String.format(Locale.ROOT, "Failed processing %s", moduleName);
            throw new MacroExecutionException(message, checkstyleException);
        }
    }
