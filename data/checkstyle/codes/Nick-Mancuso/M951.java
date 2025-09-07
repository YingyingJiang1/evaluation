    private static String getAllMatchingXpathQueriesText(Deque<DetailAST> nodes) {
        return nodes.stream()
                .map(XpathQueryGenerator::generateXpathQuery)
                .collect(Collectors.joining(NEWLINE, "", NEWLINE));
    }
