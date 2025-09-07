    private static void createListOfMessages(
            XdocSink sink, Class<?> clss, Set<String> messageKeys) {
        final String indentLevel8 = SiteUtil.getNewlineAndIndentSpaces(8);

        // This is a hack to prevent a newline from being inserted by the default sink.
        // Once we get rid of the custom parser, we can remove this.
        // until https://github.com/checkstyle/checkstyle/issues/13426
        sink.setInsertNewline(false);
        sink.list();
        sink.setInsertNewline(true);

        for (String messageKey : messageKeys) {
            createListItem(sink, clss, messageKey);
        }
        sink.rawText(indentLevel8);
        sink.list_();
    }
