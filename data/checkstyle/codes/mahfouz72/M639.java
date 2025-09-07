    private static boolean isMethodInvocation(DetailAST identAst) {
        final DetailAST parent = identAst.getParent();
        return parent.getType() == TokenTypes.DOT
                && identAst.equals(parent.getFirstChild());
    }
