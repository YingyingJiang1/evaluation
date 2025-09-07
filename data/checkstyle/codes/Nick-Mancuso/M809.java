    @Override
    public DetailAstImpl visitIfStat(JavaLanguageParser.IfStatContext ctx) {
        final DetailAstImpl ifStat = create(ctx.LITERAL_IF());
        // child[0] is 'LITERAL_IF'
        processChildren(ifStat, ctx.children.subList(1, ctx.children.size()));
        return ifStat;
    }
