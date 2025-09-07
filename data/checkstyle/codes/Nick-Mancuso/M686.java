    private static boolean containsDefaultCaseLabelElement(DetailAST detailAst) {
        return TokenUtil.findFirstTokenByPredicate(detailAst, ast -> {
            return ast.getFirstChild() != null
                    && ast.getFirstChild().findFirstToken(TokenTypes.LITERAL_DEFAULT) != null;
        }).isPresent();
    }
