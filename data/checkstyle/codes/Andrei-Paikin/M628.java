    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getFirstChild().getType() == TokenTypes.LPAREN
                && ast.findFirstToken(TokenTypes.ELIST).getChildCount() == 0) {
            log(ast, MSG_CTOR);
        }
    }
