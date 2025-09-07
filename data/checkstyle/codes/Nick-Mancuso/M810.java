    @Override
    public DetailAstImpl visitForStat(JavaLanguageParser.ForStatContext ctx) {
        final DetailAstImpl forInit = create(ctx.start);
        // child[0] is LITERAL_FOR
        processChildren(forInit, ctx.children.subList(1, ctx.children.size()));
        return forInit;
    }
