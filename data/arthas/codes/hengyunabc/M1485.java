    public static Set<Class<?>> searchClass(Instrumentation inst, String classPattern, boolean isRegEx) {
        Matcher<String> classNameMatcher = classNameMatcher(classPattern, isRegEx);
        return GlobalOptions.isDisableSubClass ? searchClass(inst, classNameMatcher) :
                searchSubClass(inst, searchClass(inst, classNameMatcher));
    }
