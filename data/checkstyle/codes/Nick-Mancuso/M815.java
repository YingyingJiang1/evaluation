    @Override
    public DetailAstImpl visitYieldStat(JavaLanguageParser.YieldStatContext ctx) {
        final DetailAstImpl yieldParent = create(ctx.LITERAL_YIELD());
        // LITERAL_YIELD is child[0]
        processChildren(yieldParent, ctx.children.subList(1, ctx.children.size()));
        return yieldParent;
    }
