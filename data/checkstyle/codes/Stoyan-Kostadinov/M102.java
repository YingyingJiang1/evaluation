    private static void writePropertySinceVersionCell(Sink sink, String propertyName,
                                                      DetailNode moduleJavadoc,
                                                      DetailNode propertyJavadoc)
            throws MacroExecutionException {
        sink.rawText(INDENT_LEVEL_14);
        sink.tableCell();
        final String sinceVersion = SiteUtil.getSinceVersion(
                currentModuleName, moduleJavadoc, propertyName, propertyJavadoc);
        sink.text(sinceVersion);
        sink.tableCell_();
    }
