    @Override
    protected Matcher getClassNameMatcher() {
        if (classNameMatcher == null) {
            if (pathPatterns == null || pathPatterns.isEmpty()) {
                classNameMatcher = SearchUtils.classNameMatcher(getClassPattern(), isRegEx());
            } else {
                classNameMatcher = getPathTracingClassMatcher();
            }
        }
        return classNameMatcher;
    }
