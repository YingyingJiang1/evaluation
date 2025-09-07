    private static void writePropertiesTable(XdocSink sink)
            throws MacroExecutionException {
        sink.table();
        sink.setInsertNewline(false);
        sink.tableRows(null, false);
        sink.rawText(INDENT_LEVEL_12);
        writeTableHeaderRow(sink);
        writeTablePropertiesRows(sink);
        sink.rawText(INDENT_LEVEL_10);
        sink.tableRows_();
        sink.table_();
        sink.setInsertNewline(true);
    }
