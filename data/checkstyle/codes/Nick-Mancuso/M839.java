    @Override
    public DetailAstImpl visitForFor(JavaLanguageParser.ForForContext ctx) {
        final DetailAstImpl dummyRoot = new DetailAstImpl();
        dummyRoot.addChild(create(ctx.LPAREN()));

        if (ctx.forInit() == null) {
            final DetailAstImpl imaginaryForInitParent =
                    createImaginary(TokenTypes.FOR_INIT);
            dummyRoot.addChild(imaginaryForInitParent);
        }
        else {
            dummyRoot.addChild(visit(ctx.forInit()));
        }

        dummyRoot.addChild(create(ctx.SEMI(0)));

        final DetailAstImpl forCondParent = createImaginary(TokenTypes.FOR_CONDITION);
        forCondParent.addChild(visit(ctx.forCond));
        dummyRoot.addChild(forCondParent);
        dummyRoot.addChild(create(ctx.SEMI(1)));

        final DetailAstImpl forItParent = createImaginary(TokenTypes.FOR_ITERATOR);
        forItParent.addChild(visit(ctx.forUpdate));
        dummyRoot.addChild(forItParent);

        dummyRoot.addChild(create(ctx.RPAREN()));

        return dummyRoot.getFirstChild();
    }
