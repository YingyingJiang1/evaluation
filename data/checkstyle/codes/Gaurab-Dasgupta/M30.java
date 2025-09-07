    private static String cleanDefaultTokensText(String initialText) {
        final Set<String> tokens = new LinkedHashSet<>();
        final Matcher matcher = TOKEN_TEXT_PATTERN.matcher(initialText);
        while (matcher.find()) {
            tokens.add(matcher.group(0));
        }
        return String.join(",", tokens);
    }
