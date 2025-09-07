    private void checkLineWrappedLambda(final boolean isSwitchRuleLambda,
                                        final int mainAstColumnNo) {
        final IndentLevel level;
        final DetailAST mainAst = getMainAst();

        if (isSwitchRuleLambda) {
            // We check the indentation of the case literal or default literal
            // on the previous line and use that to determine the correct
            // indentation for the line wrapped "->"
            final DetailAST previousSibling = mainAst.getPreviousSibling();
            final int previousLineStart = getLineStart(previousSibling);

            level = new IndentLevel(new IndentLevel(previousLineStart),
                    getIndentCheck().getLineWrappingIndentation());
        }
        else {
            level = new IndentLevel(getIndent(),
                getIndentCheck().getLineWrappingIndentation());
        }

        if (isNonAcceptableIndent(mainAstColumnNo, level)) {
            isLambdaCorrectlyIndented = false;
            logError(mainAst, "", mainAstColumnNo, level);
        }
    }
