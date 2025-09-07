    private static boolean canBeSubclassed(DetailAST classDef) {
        final DetailAST modifiers = classDef.findFirstToken(TokenTypes.MODIFIERS);
        return classDef.getType() != TokenTypes.ENUM_DEF
            && modifiers.findFirstToken(TokenTypes.FINAL) == null
            && hasDefaultOrExplicitNonPrivateCtor(classDef);
    }
