    @Override
    public DetailAstImpl visitLabelStat(JavaLanguageParser.LabelStatContext ctx) {
        final DetailAstImpl labelStat = create(TokenTypes.LABELED_STAT,
                (Token) ctx.COLON().getPayload());
        labelStat.addChild(visit(ctx.id()));
        labelStat.addChild(visit(ctx.statement()));
        return labelStat;
    }
