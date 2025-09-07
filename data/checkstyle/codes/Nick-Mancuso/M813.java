    @Override
    public DetailAstImpl visitTryStat(JavaLanguageParser.TryStatContext ctx) {
        final DetailAstImpl tryStat = create(ctx.start);
        // child[0] is 'LITERAL_TRY'
        processChildren(tryStat, ctx.children.subList(1, ctx.children.size()));
        return tryStat;
    }
