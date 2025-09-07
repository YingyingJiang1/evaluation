    private DetailAstImpl create(int tokenType, Token startToken) {
        final DetailAstImpl ast = create(startToken);
        ast.setType(tokenType);
        return ast;
    }
