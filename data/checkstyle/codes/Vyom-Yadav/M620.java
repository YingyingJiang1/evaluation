    private static boolean isStandAloneIncrementOrDecrement(DetailAST identAst) {
        final DetailAST parent = identAst.getParent();
        final DetailAST grandParent = parent.getParent();
        return TokenUtil.isOfType(parent, INCREMENT_AND_DECREMENT_TOKENS)
                && TokenUtil.isOfType(grandParent, TokenTypes.EXPR)
                && !isIncrementOrDecrementVariableUsed(grandParent);
    }
