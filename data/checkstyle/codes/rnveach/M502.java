    private int expandedTabsColumnNo(DetailAST ast) {
        final String line =
            indentCheck.getLine(ast.getLineNo() - 1);

        return CommonUtil.lengthExpandedTabs(line, ast.getColumnNo(),
            indentCheck.getIndentationTabWidth());
    }
