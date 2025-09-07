    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST closingParen = ast.getLastChild();
        final DetailAST tokenBeforeCloseParen = closingParen.getPreviousSibling();
        if (tokenBeforeCloseParen.getType() == TokenTypes.SEMI
            && (!allowWhenNoBraceAfterSemicolon
                || TokenUtil.areOnSameLine(closingParen, tokenBeforeCloseParen))) {
            log(tokenBeforeCloseParen, MSG_SEMI);
        }
    }
