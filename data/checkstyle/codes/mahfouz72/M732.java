    @Override
    public void visitToken(DetailAST ast) {
        final boolean hasPatternLabel = hasPatternLabel(ast);
        final DetailAST statementList = getStatementList(ast);
        // until https://github.com/checkstyle/checkstyle/issues/15270
        final boolean isInSwitchRule = ast.getParent().getType() == TokenTypes.SWITCH_RULE;

        if (hasPatternLabel && statementList != null && isInSwitchRule) {
            final List<DetailAST> blockStatements = getBlockStatements(statementList);

            final boolean hasAcceptableStatementsOnly = blockStatements.stream()
                    .allMatch(WhenShouldBeUsedCheck::isAcceptableStatement);

            final boolean hasSingleIfWithNoElse = blockStatements.stream()
                    .filter(WhenShouldBeUsedCheck::isSingleIfWithNoElse)
                    .count() == 1;

            if (hasAcceptableStatementsOnly && hasSingleIfWithNoElse) {
                log(ast, MSG_KEY);
            }
        }
    }
