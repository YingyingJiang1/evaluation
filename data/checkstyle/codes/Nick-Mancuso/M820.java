    @Override
    public DetailAstImpl visitContinueStat(JavaLanguageParser.ContinueStatContext ctx) {
        final DetailAstImpl continueStat = create(ctx.LITERAL_CONTINUE());
        // child[0] is 'LITERAL_CONTINUE'
        processChildren(continueStat, ctx.children.subList(1, ctx.children.size()));
        return continueStat;
    }
