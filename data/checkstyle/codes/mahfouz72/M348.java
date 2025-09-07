    private static boolean isQualifiedIdentifier(DetailAST ast) {
        final DetailAST parent = ast.getParent();
        final int parentType = parent.getType();

        final boolean isQualifiedIdent = parentType == TokenTypes.DOT
                && !TokenUtil.isOfType(ast.getPreviousSibling(), TokenTypes.DOT)
                && ast.getNextSibling() != null;
        final boolean isQualifiedIdentFromMethodRef = parentType == TokenTypes.METHOD_REF
                && ast.getNextSibling() != null;
        return isQualifiedIdent || isQualifiedIdentFromMethodRef;
    }
