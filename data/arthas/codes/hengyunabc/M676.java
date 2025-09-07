    @Override
    protected Matcher getMethodNameMatcher() {
        if (methodNameMatcher == null) {
            if (pathPatterns == null || pathPatterns.isEmpty()) {
                methodNameMatcher = SearchUtils.classNameMatcher(getMethodPattern(), isRegEx());
            } else {
                methodNameMatcher = getPathTracingMethodMatcher();
            }
        }
        return methodNameMatcher;
    }
