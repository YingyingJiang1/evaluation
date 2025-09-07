    @Override
    public DetailAstImpl visitMultiLambdaParam(JavaLanguageParser.MultiLambdaParamContext ctx) {
        final DetailAstImpl lparen = create(ctx.LPAREN());
        addLastSibling(lparen, visit(ctx.multiLambdaParams()));
        addLastSibling(lparen, create(ctx.RPAREN()));
        return lparen;
    }
