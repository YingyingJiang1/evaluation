    @Override
    public DetailAstImpl visitThrowStat(JavaLanguageParser.ThrowStatContext ctx) {
        final DetailAstImpl throwStat = create(ctx.LITERAL_THROW());
        // child[0] is 'LITERAL_THROW'
        processChildren(throwStat, ctx.children.subList(1, ctx.children.size()));
        return throwStat;
    }
