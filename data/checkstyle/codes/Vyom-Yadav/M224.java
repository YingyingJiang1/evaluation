    public static int typeDeclarationNameMatchingCount(String patternClass,
                                                       String classToBeMatched) {
        final int length = Math.min(classToBeMatched.length(), patternClass.length());
        int result = 0;
        for (int i = 0; i < length && patternClass.charAt(i) == classToBeMatched.charAt(i); ++i) {
            if (patternClass.charAt(i) == PACKAGE_SEPARATOR) {
                result = i;
            }
        }
        return result;
    }
