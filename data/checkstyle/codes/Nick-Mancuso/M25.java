    private static void appendBrackets(FullIdent full, DetailAST ast) {
        final int bracketCount =
                ast.getParent().getChildCount(TokenTypes.ARRAY_DECLARATOR);
        for (int i = 0; i < bracketCount; i++) {
            full.append("[]");
        }
    }
