    private static boolean hasNullCaseLabel(DetailAST detailAST) {
        return TokenUtil.findFirstTokenByPredicate(detailAST.getParent(), ast -> {
            final DetailAST expr = ast.findFirstToken(TokenTypes.EXPR);
            return expr != null && expr.findFirstToken(TokenTypes.LITERAL_NULL) != null;
        }).isPresent();
    }
