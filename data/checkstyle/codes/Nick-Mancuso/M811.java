    @Override
    public DetailAstImpl visitWhileStat(JavaLanguageParser.WhileStatContext ctx) {
        final DetailAstImpl whileStatement = create(ctx.start);
        // 'LITERAL_WHILE' is child[0]
        processChildren(whileStatement, ctx.children.subList(1, ctx.children.size()));
        return whileStatement;
    }
