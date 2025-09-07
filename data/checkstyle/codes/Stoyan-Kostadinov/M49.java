    private static String getCodeSnippet(Collection<String> lines) {
        return lines.stream()
                .dropWhile(line -> !line.contains(CODE_SNIPPET_START))
                .skip(1)
                .takeWhile(line -> !line.contains(CODE_SNIPPET_END))
                .collect(Collectors.joining(NEWLINE));
    }
