    public static String stripIndentAndInitialNewLineFromTextBlock(String textBlockContent) {
        final String contentWithInitialNewLineRemoved =
            ALL_NEW_LINES.matcher(textBlockContent).replaceFirst("");
        final List<String> lines =
            Arrays.asList(ALL_NEW_LINES.split(contentWithInitialNewLineRemoved));
        final int indent = getSmallestIndent(lines);
        final String suffix = "";

        return lines.stream()
                .map(line -> stripIndentAndTrailingWhitespaceFromLine(line, indent))
                .collect(Collectors.joining(System.lineSeparator(), suffix, suffix));
    }
