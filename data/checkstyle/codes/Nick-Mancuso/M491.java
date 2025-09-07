    @Override
    public void checkIndentation() {
        checkYield();
        final DetailAST expression = getMainAst().getFirstChild();
        if (!TokenUtil.areOnSameLine(getMainAst(), expression)) {
            checkExpressionSubtree(expression, getIndent(), false, false);
        }
    }
