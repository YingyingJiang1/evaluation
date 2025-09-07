    @Override
    public DetailAstImpl visitPrimitivePrimary(JavaLanguageParser.PrimitivePrimaryContext ctx) {
        final DetailAstImpl dot = create(ctx.DOT());
        final DetailAstImpl primaryTypeNoArray = visit(ctx.type);
        dot.addChild(primaryTypeNoArray);
        ctx.arrayDeclarator().forEach(child -> dot.addChild(visit(child)));
        dot.addChild(create(ctx.LITERAL_CLASS()));
        return dot;
    }
