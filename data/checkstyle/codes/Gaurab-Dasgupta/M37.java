    private ModuleType getModuleType() {
        final String simpleModuleName = getModuleSimpleName();
        final ModuleType result;
        if (simpleModuleName.endsWith("FileFilter")) {
            result = ModuleType.FILEFILTER;
        }
        else if (simpleModuleName.endsWith("Filter")) {
            result = ModuleType.FILTER;
        }
        else {
            result = ModuleType.CHECK;
        }
        return result;
    }
