    public String getClassName() {
        String className = getName();
        className = className.replace(DIR_SEPARATOR, PKG_SEPARATOR);
        className = className.substring(1, className.indexOf(CLASS_FILE_SUFFIX));
        return className;
    }
