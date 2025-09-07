    @Override
    public DetailAstImpl visitSwitchExpressionOrStatement(
            JavaLanguageParser.SwitchExpressionOrStatementContext ctx) {
        final DetailAstImpl switchStat = create(ctx.LITERAL_SWITCH());
        switchStat.addChild(visit(ctx.parExpression()));
        switchStat.addChild(create(ctx.LCURLY()));
        switchStat.addChild(visit(ctx.switchBlock()));
        switchStat.addChild(create(ctx.RCURLY()));
        return switchStat;
    }
