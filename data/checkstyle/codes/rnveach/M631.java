    private static boolean isHashCodeMethod(DetailAST ast) {
        final DetailAST modifiers = ast.getFirstChild();
        final DetailAST methodName = ast.findFirstToken(TokenTypes.IDENT);
        final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

        return "hashCode".equals(methodName.getText())
                && parameters.getFirstChild() == null
                && (ast.findFirstToken(TokenTypes.SLIST) != null
                        || modifiers.findFirstToken(TokenTypes.LITERAL_NATIVE) != null);
    }
