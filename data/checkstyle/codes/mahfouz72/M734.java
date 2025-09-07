    private static boolean isAcceptableStatement(DetailAST ast) {
        final int[] acceptableChildrenOfSlist = {
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_BREAK,
            TokenTypes.EMPTY_STAT,
            TokenTypes.RCURLY,
        };
        return TokenUtil.isOfType(ast, acceptableChildrenOfSlist);
    }
