    private static int getFirstNodeLineNumber(DetailAST lambdaBody) {
        DetailAST node = lambdaBody;
        int result;
        do {
            result = node.getLineNo();
            node = node.getFirstChild();
        } while (node != null);
        return result;
    }
