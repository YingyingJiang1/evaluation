    private static DetailAST findScopeOfVariable(DetailAST variableDef) {
        final DetailAST result;
        final DetailAST parentAst = variableDef.getParent();
        if (TokenUtil.isOfType(parentAst, TokenTypes.SLIST, TokenTypes.OBJBLOCK)) {
            result = parentAst;
        }
        else {
            result = parentAst.getParent();
        }
        return result;
    }
