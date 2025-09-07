    public static boolean isBooleanLiteralType(final int tokenType) {
        final boolean isTrue = tokenType == TokenTypes.LITERAL_TRUE;
        final boolean isFalse = tokenType == TokenTypes.LITERAL_FALSE;
        return isTrue || isFalse;
    }
