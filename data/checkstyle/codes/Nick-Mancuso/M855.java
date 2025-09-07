    @Override
    public DetailAstImpl visitThisExp(JavaLanguageParser.ThisExpContext ctx) {
        final DetailAstImpl bop = create(ctx.bop);
        bop.addChild(visit(ctx.expr()));
        bop.addChild(create(ctx.LITERAL_THIS()));
        return bop;
    }
