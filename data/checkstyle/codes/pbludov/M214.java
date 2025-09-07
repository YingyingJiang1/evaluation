    public static Scope getScope(DetailAST ast) {
        return Optional.ofNullable(ast.findFirstToken(TokenTypes.MODIFIERS))
                .map(ScopeUtil::getDeclaredScopeFromMods)
                .orElseGet(() -> getDefaultScope(ast));
    }
