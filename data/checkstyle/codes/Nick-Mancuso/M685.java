    private static boolean containsDefaultLabel(DetailAST detailAst) {
        return TokenUtil.findFirstTokenByPredicate(detailAst,
                ast -> ast.findFirstToken(TokenTypes.LITERAL_DEFAULT) != null
        ).isPresent();
    }
