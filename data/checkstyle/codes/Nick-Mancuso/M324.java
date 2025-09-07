    private static boolean isMethodOrCtorOrInitDefinition(int tokenType) {
        return tokenType == TokenTypes.METHOD_DEF
                || tokenType == TokenTypes.COMPACT_CTOR_DEF
                || tokenType == TokenTypes.CTOR_DEF
                || tokenType == TokenTypes.STATIC_INIT
                || tokenType == TokenTypes.INSTANCE_INIT;
    }
