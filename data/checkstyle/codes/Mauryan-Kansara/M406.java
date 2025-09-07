    @Override
    public void finishJavadocTree(DetailNode rootAst) {
        // this method checks the alignment of closing javadoc tag.
        final DetailAST javadocEndToken = getBlockCommentAst().getLastChild();
        final String lastLine = fileLines[javadocEndToken.getLineNo() - 1];
        final Optional<Integer> endingBlockColumnNumber = getAsteriskColumnNumber(lastLine);

        endingBlockColumnNumber
                .map(columnNumber -> expandedTabs(lastLine, columnNumber))
                .filter(columnNumber -> {
                    return !hasValidAlignment(expectedColumnNumberTabsExpanded, columnNumber);
                })
                .ifPresent(columnNumber -> {
                    logViolation(javadocEndToken.getLineNo(),
                            columnNumber,
                            expectedColumnNumberTabsExpanded);
                });
    }
