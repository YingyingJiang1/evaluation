    private static void writeLinkToToken(Sink sink, String document, String tokenName)
            throws MacroExecutionException {
        final String link = SiteUtil.getLinkToDocument(currentModuleName, document)
                        + "#" + tokenName;
        sink.link(link);
        sink.rawText(INDENT_LEVEL_20);
        sink.text(tokenName);
        sink.link_();
    }
