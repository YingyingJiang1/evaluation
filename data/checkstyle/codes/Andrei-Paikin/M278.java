    private static int getLastNodeLineNumber(DetailAST lambdaBody) {
        DetailAST node = lambdaBody;
        int result;
        do {
            result = node.getLineNo();
            node = node.getLastChild();
        } while (node != null);
        return result;
    }
