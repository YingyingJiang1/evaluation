    private static void writePropertyDescriptionCell(Sink sink, String propertyName,
                                                     DetailNode propertyJavadoc)
            throws MacroExecutionException {
        sink.rawText(INDENT_LEVEL_14);
        sink.tableCell();
        final String description = SiteUtil
                .getPropertyDescription(propertyName, propertyJavadoc, currentModuleName);

        sink.rawText(description);
        sink.tableCell_();
    }
