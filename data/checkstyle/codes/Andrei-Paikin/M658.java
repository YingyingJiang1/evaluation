    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getParent().getType() == TokenTypes.LITERAL_NEW
            && hasOnlyInitialization(ast)) {
            log(ast, MSG_KEY);
        }
    }
