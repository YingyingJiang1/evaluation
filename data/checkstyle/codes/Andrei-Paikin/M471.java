    private static DetailAST getLastLambdaToken(DetailAST lambda) {
        DetailAST node = lambda;
        do {
            node = node.getLastChild();
        } while (node.getLastChild() != null);
        return node;
    }
