    private static boolean isSuppressedAfterEventStart(int line, int column, Entry entry) {
        return entry.getFirstLine() < line
            || entry.getFirstLine() == line
            && (column == 0 || entry.getFirstColumn() <= column);
    }
