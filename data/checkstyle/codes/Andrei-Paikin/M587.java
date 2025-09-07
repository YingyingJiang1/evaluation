    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST enumBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        final DetailAST semicolon = enumBlock.findFirstToken(TokenTypes.SEMI);
        if (semicolon != null && isEndOfEnumerationAfter(semicolon)) {
            log(semicolon, MSG_SEMI);
        }
    }
