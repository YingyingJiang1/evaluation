    private static boolean isSuppressedBeforeEventEnd(int line, int column, Entry entry) {
        return entry.getLastLine() > line
            || entry.getLastLine() == line && entry
                .getLastColumn() >= column;
    }
