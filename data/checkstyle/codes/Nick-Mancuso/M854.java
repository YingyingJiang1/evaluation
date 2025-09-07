    @Override
    public DetailAstImpl visitLambdaExp(JavaLanguageParser.LambdaExpContext ctx) {
        final DetailAstImpl lambda = create(ctx.LAMBDA());
        lambda.addChild(visit(ctx.lambdaParameters()));

        final JavaLanguageParser.BlockContext blockContext = ctx.block();
        final DetailAstImpl rightHandLambdaChild;
        if (blockContext != null) {
            rightHandLambdaChild = visit(blockContext);
        }
        else {
            // Lambda expression child is built the same way that we build
            // the initial expression node in visitExpression, i.e. with
            // an imaginary EXPR node. This results in nested EXPR nodes
            // in the AST.
            rightHandLambdaChild = buildExpressionNode(ctx.expr());
        }
        lambda.addChild(rightHandLambdaChild);
        return lambda;
    }
