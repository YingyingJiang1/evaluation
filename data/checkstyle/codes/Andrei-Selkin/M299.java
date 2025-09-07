    private static DetailAST getGenericTypeArgs(DetailAST type, boolean isCanonicalName) {
        final DetailAST typeArgs;
        if (isCanonicalName) {
            // if type class name is in canonical form, abstract tree has specific structure
            typeArgs = type.getFirstChild().findFirstToken(TokenTypes.TYPE_ARGUMENTS);
        }
        else {
            typeArgs = type.findFirstToken(TokenTypes.TYPE_ARGUMENTS);
        }
        return typeArgs;
    }
