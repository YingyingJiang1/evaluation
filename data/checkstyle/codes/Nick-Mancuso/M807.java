    @Override
    public DetailAstImpl visitBlock(JavaLanguageParser.BlockContext ctx) {
        final DetailAstImpl slist = create(TokenTypes.SLIST, ctx.start);
        // SLIST was child [0]
        processChildren(slist, ctx.children.subList(1, ctx.children.size()));
        return slist;
    }
