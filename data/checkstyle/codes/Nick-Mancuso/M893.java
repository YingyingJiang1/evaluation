    private DetailAstImpl buildExpressionNode(ParseTree exprNode) {
        final DetailAstImpl expression = visit(exprNode);

        final DetailAstImpl exprRoot;
        if (TokenUtil.isOfType(expression, EXPRESSIONS_WITH_NO_EXPR_ROOT)) {
            exprRoot = expression;
        }
        else {
            // create imaginary 'EXPR' node as root of expression
            exprRoot = createImaginary(TokenTypes.EXPR);
            exprRoot.addChild(expression);
        }
        return exprRoot;
    }
