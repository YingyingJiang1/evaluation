    @Override
    public DetailAstImpl visitBracketsWithExp(JavaLanguageParser.BracketsWithExpContext ctx) {
        final DetailAstImpl dummyRoot = new DetailAstImpl();
        dummyRoot.addChild(visit(ctx.annotations()));
        final DetailAstImpl arrayDeclarator =
                create(TokenTypes.ARRAY_DECLARATOR, (Token) ctx.LBRACK().getPayload());
        arrayDeclarator.addChild(visit(ctx.expression()));
        arrayDeclarator.addChild(create(ctx.stop));
        dummyRoot.addChild(arrayDeclarator);
        return dummyRoot.getFirstChild();
    }
