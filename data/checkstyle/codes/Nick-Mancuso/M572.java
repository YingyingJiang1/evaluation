    private static boolean isInPatternDefinition(DetailAST node) {
        DetailAST parent = node;
        final int[] tokensToStopOn = {
            // token we are looking for
            TokenTypes.PATTERN_DEF,
            // tokens that mean we can stop looking
            TokenTypes.EXPR,
            TokenTypes.RESOURCE,
            TokenTypes.COMPILATION_UNIT,
        };

        do {
            parent = parent.getParent();
        } while (!TokenUtil.isOfType(parent, tokensToStopOn));
        return parent.getType() == TokenTypes.PATTERN_DEF;
    }
