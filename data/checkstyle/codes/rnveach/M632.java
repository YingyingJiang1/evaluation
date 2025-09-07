    private static boolean isObjectParam(DetailAST paramNode) {
        final DetailAST typeNode = paramNode.findFirstToken(TokenTypes.TYPE);
        final FullIdent fullIdent = FullIdent.createFullIdentBelow(typeNode);
        final String name = fullIdent.getText();
        return "Object".equals(name) || "java.lang.Object".equals(name);
    }
