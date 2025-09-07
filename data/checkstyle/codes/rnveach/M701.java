    private void visitReturn(DetailAST ast) {
        // we can't identify which max to use for lambdas, so we can only assign
        // after the first return statement is seen
        if (ast.getFirstChild().getType() == TokenTypes.SEMI) {
            context.visitLiteralReturn(maxForVoid, Boolean.TRUE);
        }
        else {
            context.visitLiteralReturn(max, Boolean.FALSE);
        }
    }
