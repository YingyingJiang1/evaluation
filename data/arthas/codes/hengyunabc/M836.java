    private Matcher<String> methodNameMatcher() {
        // auto fix default methodPattern
        if (StringUtils.isBlank(methodPattern)) {
            methodPattern = isRegEx ? ".*" : "*";
        }
        return isRegEx ? new RegexMatcher(methodPattern) : new WildcardMatcher(methodPattern);
    }
