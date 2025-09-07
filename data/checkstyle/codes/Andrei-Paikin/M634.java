    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST nextSibling = ast.getNextSibling();
        if (nextSibling != null
                && ScopeUtil.isOuterMostType(ast)
                && nextSibling.getType() == TokenTypes.SEMI) {
            log(nextSibling, MSG_SEMI);
        }
    }
