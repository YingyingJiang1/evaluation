    @Override
    public DetailAstImpl visitSyncStat(JavaLanguageParser.SyncStatContext ctx) {
        final DetailAstImpl syncStatement = create(ctx.start);
        // child[0] is 'LITERAL_SYNCHRONIZED'
        processChildren(syncStatement, ctx.children.subList(1, ctx.children.size()));
        return syncStatement;
    }
