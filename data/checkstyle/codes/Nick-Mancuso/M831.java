    @Override
    public DetailAstImpl visitFinallyBlock(JavaLanguageParser.FinallyBlockContext ctx) {
        final DetailAstImpl finallyBlock = create(ctx.LITERAL_FINALLY());
        // child[0] is 'LITERAL_FINALLY'
        processChildren(finallyBlock, ctx.children.subList(1, ctx.children.size()));
        return finallyBlock;
    }
