    private void checkWhileExpr() {
        // check while statement alone

        final DetailAST whileAst = getMainAst().findFirstToken(TokenTypes.DO_WHILE);

        if (isOnStartOfLine(whileAst)
                && !getIndent().isAcceptable(expandedTabsColumnNo(whileAst))) {
            logError(whileAst, "while", expandedTabsColumnNo(whileAst));
        }

        // check condition alone

        final DetailAST condAst = getMainAst().findFirstToken(TokenTypes.LPAREN).getNextSibling();

        checkExpressionSubtree(condAst, getIndent(), false, false);
    }
