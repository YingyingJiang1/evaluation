    @Override
    protected Matcher getClassNameExcludeMatcher() {
        if (classNameExcludeMatcher == null && getExcludeClassPattern() != null) {
            classNameExcludeMatcher = SearchUtils.classNameMatcher(getExcludeClassPattern(), isRegEx());
        }
        return classNameExcludeMatcher;
    }
