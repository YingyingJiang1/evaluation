    @Override
    public void beginJavadocTree(DetailNode rootAst) {
        if (isTopLevelClassJavadoc()) {
            moduleDetails = new ModuleDetails();
            toScan = false;
            scrapingViolationMessageList = false;
            propertySectionStartIdx = -1;
            exampleSectionStartIdx = -1;
            parentSectionStartIdx = -1;

            String moduleName = getModuleSimpleName();
            final String checkModuleExtension = "Check";
            if (moduleName.endsWith(checkModuleExtension)) {
                moduleName = moduleName
                        .substring(0, moduleName.length() - checkModuleExtension.length());
            }
            moduleDetails.setName(moduleName);
            moduleDetails.setFullQualifiedName(getPackageName(getFilePath()));
            moduleDetails.setModuleType(getModuleType());
        }
    }
