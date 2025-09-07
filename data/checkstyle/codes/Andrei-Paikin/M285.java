    private static void addSuppressions(List<String> values, DetailAST targetAST) {
        // get text range of target
        final int firstLine = targetAST.getLineNo();
        final int firstColumn = targetAST.getColumnNo();
        final DetailAST nextAST = targetAST.getNextSibling();
        final int lastLine;
        final int lastColumn;
        if (nextAST == null) {
            lastLine = Integer.MAX_VALUE;
            lastColumn = Integer.MAX_VALUE;
        }
        else {
            lastLine = nextAST.getLineNo();
            lastColumn = nextAST.getColumnNo();
        }

        final List<Entry> entries = ENTRIES.get();
        for (String value : values) {
            // strip off the checkstyle-only prefix if present
            final String checkName = removeCheckstylePrefixIfExists(value);
            entries.add(new Entry(checkName, firstLine, firstColumn,
                    lastLine, lastColumn));
        }
    }
