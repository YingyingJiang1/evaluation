    private void visitWhenExpression(DetailAST ast, int basicBranchingFactor) {
        final int expressionValue = basicBranchingFactor + countConditionalOperators(ast);
        processingTokenEnd.setToken(getLastToken(ast));
        pushValue(expressionValue);
    }
