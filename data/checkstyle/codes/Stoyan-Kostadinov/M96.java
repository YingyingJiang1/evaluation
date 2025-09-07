    private static void writePropertyNameCell(Sink sink, String propertyName) {
        sink.rawText(INDENT_LEVEL_14);
        sink.tableCell();
        sink.text(propertyName);
        sink.tableCell_();
    }
