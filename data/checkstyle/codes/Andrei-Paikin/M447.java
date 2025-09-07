    private static boolean isPublic(DetailAST modifiers) {
        return modifiers.findFirstToken(TokenTypes.LITERAL_PUBLIC) != null
                || ScopeUtil.isInAnnotationBlock(modifiers)
                || ScopeUtil.isInInterfaceBlock(modifiers)
                    // interface methods can be private
                    && modifiers.findFirstToken(TokenTypes.LITERAL_PRIVATE) == null;
    }
