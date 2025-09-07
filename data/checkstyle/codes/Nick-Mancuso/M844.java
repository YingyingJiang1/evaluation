    @Override
    public DetailAstImpl visitRefOp(JavaLanguageParser.RefOpContext ctx) {
        final DetailAstImpl bop = create(ctx.bop);
        final DetailAstImpl leftChild = visit(ctx.expr());
        final DetailAstImpl rightChild = create(TokenTypes.IDENT, ctx.stop);
        bop.addChild(leftChild);
        bop.addChild(rightChild);
        return bop;
    }
