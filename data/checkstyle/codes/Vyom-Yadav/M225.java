    public static String getQualifiedTypeDeclarationName(String packageName,
                                                         String outerClassQualifiedName,
                                                         String className) {
        final String qualifiedClassName;

        if (outerClassQualifiedName == null) {
            if (packageName == null) {
                qualifiedClassName = className;
            }
            else {
                qualifiedClassName = packageName + PACKAGE_SEPARATOR + className;
            }
        }
        else {
            qualifiedClassName = outerClassQualifiedName + PACKAGE_SEPARATOR + className;
        }
        return qualifiedClassName;
    }
