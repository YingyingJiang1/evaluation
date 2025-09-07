    private static boolean isAnnotationOnType(DetailAST modifier) {
        boolean annotationOnType = false;
        final DetailAST modifiers = modifier.getParent();
        final DetailAST definition = modifiers.getParent();
        final int definitionType = definition.getType();
        if (definitionType == TokenTypes.VARIABLE_DEF
                || definitionType == TokenTypes.PARAMETER_DEF
                || definitionType == TokenTypes.CTOR_DEF) {
            annotationOnType = true;
        }
        else if (definitionType == TokenTypes.METHOD_DEF) {
            final DetailAST typeToken = definition.findFirstToken(TokenTypes.TYPE);
            final int methodReturnType = typeToken.getLastChild().getType();
            if (methodReturnType != TokenTypes.LITERAL_VOID) {
                annotationOnType = true;
            }
        }
        return annotationOnType;
    }
