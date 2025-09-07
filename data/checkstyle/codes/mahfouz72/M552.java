    private boolean isEmptySwitchBlockStatement(DetailAST ast) {
        final boolean isEmptySwitchBlockStatement;

        if (allowEmptySwitchBlockStatements) {
            final DetailAST parent = ast.getParent();
            final DetailAST grandParent = parent.getParent();

            final boolean isEmptyCaseInSwitchRule =
                    isEmptyBlock(ast, parent.getType(), TokenTypes.SWITCH_RULE);

            final boolean isEmptyCaseGroupCheckedFromLcurly =
                    isEmptyBlock(ast, grandParent.getType(), TokenTypes.CASE_GROUP);

            final boolean isEmptyCaseGroupCheckedFromRcurly =
                    parent.getFirstChild().getType() == TokenTypes.RCURLY
                      && grandParent.getParent().getType() == TokenTypes.CASE_GROUP;

            isEmptySwitchBlockStatement = isEmptyCaseInSwitchRule
                    || isEmptyCaseGroupCheckedFromLcurly || isEmptyCaseGroupCheckedFromRcurly;
        }
        else {
            isEmptySwitchBlockStatement = false;
        }

        return isEmptySwitchBlockStatement;
    }
