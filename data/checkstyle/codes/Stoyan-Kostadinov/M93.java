    private static void writeTableHeaderRow(Sink sink) {
        sink.tableRow();
        writeTableHeaderCell(sink, "name");
        writeTableHeaderCell(sink, "description");
        writeTableHeaderCell(sink, "type");
        writeTableHeaderCell(sink, "default value");
        writeTableHeaderCell(sink, "since");
        sink.rawText(INDENT_LEVEL_12);
        sink.tableRow_();
    }
