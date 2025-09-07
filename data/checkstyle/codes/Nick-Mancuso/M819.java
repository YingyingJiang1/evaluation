    @Override
    public DetailAstImpl visitBreakStat(JavaLanguageParser.BreakStatContext ctx) {
        final DetailAstImpl literalBreak = create(ctx.LITERAL_BREAK());
        // child[0] is 'LITERAL_BREAK'
        processChildren(literalBreak, ctx.children.subList(1, ctx.children.size()));
        return literalBreak;
    }
