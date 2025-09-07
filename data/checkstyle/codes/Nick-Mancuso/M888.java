    private DetailAstImpl create(Token token) {
        final int tokenIndex = token.getTokenIndex();
        final List<Token> tokensToLeft =
                tokens.getHiddenTokensToLeft(tokenIndex, JavaLanguageLexer.COMMENTS);
        final List<Token> tokensToRight =
                tokens.getHiddenTokensToRight(tokenIndex, JavaLanguageLexer.COMMENTS);

        final DetailAstImpl detailAst = new DetailAstImpl();
        detailAst.initialize(token);
        if (tokensToLeft != null) {
            detailAst.setHiddenBefore(tokensToLeft);
        }
        if (tokensToRight != null) {
            detailAst.setHiddenAfter(tokensToRight);
        }
        return detailAst;
    }
