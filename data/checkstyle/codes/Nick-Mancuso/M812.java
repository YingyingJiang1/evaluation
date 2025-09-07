    @Override
    public DetailAstImpl visitDoStat(JavaLanguageParser.DoStatContext ctx) {
        final DetailAstImpl doStatement = create(ctx.start);
        // 'LITERAL_DO' is child[0]
        doStatement.addChild(visit(ctx.statement()));
        // We make 'LITERAL_WHILE' into 'DO_WHILE'
        doStatement.addChild(create(TokenTypes.DO_WHILE, (Token) ctx.LITERAL_WHILE().getPayload()));
        doStatement.addChild(visit(ctx.parExpression()));
        doStatement.addChild(create(ctx.SEMI()));
        return doStatement;
    }
