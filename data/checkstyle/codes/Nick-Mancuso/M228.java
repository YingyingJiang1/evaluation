    private static Map.Entry<Integer, Integer> countLinesColumns(
        String text, int initialLinesCnt, int initialColumnsCnt) {
        int lines = initialLinesCnt;
        int columns = initialColumnsCnt;
        boolean foundCr = false;
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                foundCr = false;
                lines++;
                columns = 0;
            }
            else {
                if (foundCr) {
                    foundCr = false;
                    lines++;
                    columns = 0;
                }
                if (c == '\r') {
                    foundCr = true;
                }
                columns++;
            }
        }
        if (foundCr) {
            lines++;
            columns = 0;
        }
        return new AbstractMap.SimpleEntry<>(lines, columns);
    }
