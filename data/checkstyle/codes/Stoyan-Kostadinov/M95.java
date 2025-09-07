    private static void writePropertyRow(Sink sink, String propertyName,
                                         DetailNode propertyJavadoc, Object instance,
                                            DetailNode moduleJavadoc)
            throws MacroExecutionException {
        final Field field = SiteUtil.getField(instance.getClass(), propertyName);

        sink.rawText(INDENT_LEVEL_12);
        sink.tableRow();

        writePropertyNameCell(sink, propertyName);
        writePropertyDescriptionCell(sink, propertyName, propertyJavadoc);
        writePropertyTypeCell(sink, propertyName, field, instance);
        writePropertyDefaultValueCell(sink, propertyName, field, instance);
        writePropertySinceVersionCell(
                sink, propertyName, moduleJavadoc, propertyJavadoc);

        sink.rawText(INDENT_LEVEL_12);
        sink.tableRow_();
    }
