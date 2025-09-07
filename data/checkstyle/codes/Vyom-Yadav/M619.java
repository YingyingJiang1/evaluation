    private static boolean isLeftHandSideValue(DetailAST identAst) {
        final DetailAST parent = identAst.getParent();
        return isStandAloneIncrementOrDecrement(identAst)
                || parent.getType() == TokenTypes.ASSIGN
                && identAst != parent.getLastChild();
    }
