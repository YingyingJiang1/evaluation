    private static DetailAST getParentAst(DetailAST ast, int type) {
        DetailAST node = ast.getParent();

        while (node != null && node.getType() != type) {
            node = node.getParent();
        }

        return node;
    }
