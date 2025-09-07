    private static String getConfigSnippet(Collection<String> lines) {
        return lines.stream()
                .dropWhile(line -> !XML_CONFIG_START.equals(line))
                .skip(1)
                .takeWhile(line -> !XML_CONFIG_END.equals(line))
                .collect(Collectors.joining(NEWLINE));
    }
