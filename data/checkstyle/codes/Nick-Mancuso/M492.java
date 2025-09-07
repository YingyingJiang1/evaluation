    private void checkYield() {
        final DetailAST yieldKey = getMainAst();
        final int columnNo = expandedTabsColumnNo(yieldKey);
        if (isOnStartOfLine(yieldKey) && !getIndent().isAcceptable(columnNo)) {
            logError(yieldKey, "", columnNo);
        }
    }
