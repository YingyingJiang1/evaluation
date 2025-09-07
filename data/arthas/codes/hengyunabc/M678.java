    private Matcher<String> getPathTracingClassMatcher() {

        List<Matcher<String>> matcherList = new ArrayList<Matcher<String>>();
        matcherList.add(SearchUtils.classNameMatcher(getClassPattern(), isRegEx()));

        if (null != getPathPatterns()) {
            for (String pathPattern : getPathPatterns()) {
                if (isRegEx()) {
                    matcherList.add(new RegexMatcher(pathPattern));
                } else {
                    matcherList.add(new WildcardMatcher(pathPattern));
                }
            }
        }

        return new GroupMatcher.Or<String>(matcherList);
    }
