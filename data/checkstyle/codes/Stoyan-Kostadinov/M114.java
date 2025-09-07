    private static void createListItem(XdocSink sink, Class<?> clss, String messageKey) {
        final String messageKeyUrl = constructMessageKeyUrl(clss, messageKey);
        final String indentLevel10 = SiteUtil.getNewlineAndIndentSpaces(10);
        final String indentLevel12 = SiteUtil.getNewlineAndIndentSpaces(12);
        final String indentLevel14 = SiteUtil.getNewlineAndIndentSpaces(14);
        // Place the <li>.
        sink.rawText(indentLevel10);
        // This is a hack to prevent a newline from being inserted by the default sink.
        // Once we get rid of the custom parser, we can remove this.
        // until https://github.com/checkstyle/checkstyle/issues/13426
        sink.setInsertNewline(false);
        sink.listItem();
        sink.setInsertNewline(true);

        // Place an <a>.
        sink.rawText(indentLevel12);
        sink.link(messageKeyUrl);
        // Further indent the text.
        sink.rawText(indentLevel14);
        sink.rawText(messageKey);

        // Place closing </a> and </li> tags.
        sink.rawText(indentLevel12);
        sink.link_();
        sink.rawText(indentLevel10);
        sink.listItem_();
    }
