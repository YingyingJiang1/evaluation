    private static boolean isLeftHandOfAssignment(DetailAST identAst) {
        final DetailAST parent = identAst.getParent();
        return parent.getType() == TokenTypes.ASSIGN
                && !identAst.equals(parent.getLastChild());
    }
