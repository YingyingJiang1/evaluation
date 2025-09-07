    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        final boolean isSealed = modifiers.findFirstToken(TokenTypes.LITERAL_SEALED) != null;
        final boolean hasPermitsList = ast.findFirstToken(TokenTypes.PERMITS_CLAUSE) != null;

        if (isSealed && !hasPermitsList) {
            log(ast, MSG_KEY);
        }
    }
