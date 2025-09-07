    @Override
    public DetailAstImpl visitTryWithResourceStat(
            JavaLanguageParser.TryWithResourceStatContext ctx) {
        final DetailAstImpl tryWithResources = create(ctx.LITERAL_TRY());
        // child[0] is 'LITERAL_TRY'
        processChildren(tryWithResources, ctx.children.subList(1, ctx.children.size()));
        return tryWithResources;
    }
