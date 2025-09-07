    private static String getContentWithoutPrecedingWhitespace(String textBlockContent) {
        final String contentWithNoPrecedingNewline =
            NEWLINE.matcher(textBlockContent).replaceAll("");
        return WHITESPACE.matcher(contentWithNoPrecedingNewline).replaceAll("");
    }
