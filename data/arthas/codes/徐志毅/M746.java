    private Matcher<String> getAttributeMatcher() {
        if (StringUtils.isEmpty(attribute)) {
            attribute = isRegEx ? ".*" : "*";
        }
        return isRegEx ? new RegexMatcher(attribute) : new WildcardMatcher(attribute);
    }
