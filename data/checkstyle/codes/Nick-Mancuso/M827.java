    @Override
    public DetailAstImpl visitElseStat(JavaLanguageParser.ElseStatContext ctx) {
        final DetailAstImpl elseStat = create(ctx.LITERAL_ELSE());
        // child[0] is 'LITERAL_ELSE'
        processChildren(elseStat, ctx.children.subList(1, ctx.children.size()));
        return elseStat;
    }
