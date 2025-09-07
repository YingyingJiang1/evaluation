    private List<MatchInfo> findOccurrencesInText(String searchText, String content) {
        List<MatchInfo> matches = new ArrayList<>();

        Pattern pattern;

        if (useRegex) {
            // Use regex-based search
            pattern =
                    wholeWordSearch
                            ? Pattern.compile("\\b" + searchText + "\\b")
                            : Pattern.compile(searchText);
        } else {
            // Use normal text search
            pattern =
                    wholeWordSearch
                            ? Pattern.compile("\\b" + Pattern.quote(searchText) + "\\b")
                            : Pattern.compile(Pattern.quote(searchText));
        }

        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            matches.add(new MatchInfo(matcher.start(), matcher.end() - matcher.start()));
        }
        return matches;
    }
