    private static int getLength(DetailAST ast) {
        final DetailAST lambdaBody = ast.getLastChild();
        final int length;
        if (lambdaBody.getType() == TokenTypes.SLIST) {
            length = lambdaBody.getLastChild().getLineNo() - lambdaBody.getLineNo();
        }
        else {
            length = getLastNodeLineNumber(lambdaBody) - getFirstNodeLineNumber(lambdaBody);
        }
        return length + 1;
    }
