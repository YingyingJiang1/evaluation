    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getParent().getType() != TokenTypes.SWITCH_RULE) {
            final int length = getLength(ast);
            if (length > max) {
                log(ast, MSG_KEY, length, max);
            }
        }
    }
