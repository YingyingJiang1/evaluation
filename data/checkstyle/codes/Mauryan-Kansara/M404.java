    @Override
    public void beginJavadocTree(DetailNode rootAst) {
        // this method processes and sets information of starting javadoc tag.
        fileLines = getLines();
        final String startLine = fileLines[rootAst.getLineNumber() - 1];
        javadocStartLineNumber = rootAst.getLineNumber();
        expectedColumnNumberTabsExpanded = CommonUtil.lengthExpandedTabs(
            startLine, rootAst.getColumnNumber() - 1, getTabWidth());
    }
