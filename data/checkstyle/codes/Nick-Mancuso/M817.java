    @Override
    public DetailAstImpl visitReturnStat(JavaLanguageParser.ReturnStatContext ctx) {
        final DetailAstImpl returnStat = create(ctx.LITERAL_RETURN());
        // child[0] is 'LITERAL_RETURN'
        processChildren(returnStat, ctx.children.subList(1, ctx.children.size()));
        return returnStat;
    }
