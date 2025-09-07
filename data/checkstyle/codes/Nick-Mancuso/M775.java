    @Override
    public DetailAstImpl visitThrowsList(JavaLanguageParser.ThrowsListContext ctx) {
        final DetailAstImpl throwsRoot = create(ctx.LITERAL_THROWS());
        throwsRoot.addChild(visit(ctx.qualifiedNameList()));
        return throwsRoot;
    }
