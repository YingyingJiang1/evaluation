    @Override
    public DetailAstImpl visitExplicitCtorCall(JavaLanguageParser.ExplicitCtorCallContext ctx) {
        final DetailAstImpl root;
        if (ctx.LITERAL_THIS() == null) {
            root = create(TokenTypes.SUPER_CTOR_CALL, (Token) ctx.LITERAL_SUPER().getPayload());
        }
        else {
            root = create(TokenTypes.CTOR_CALL, (Token) ctx.LITERAL_THIS().getPayload());
        }
        root.addChild(visit(ctx.typeArguments()));
        root.addChild(visit(ctx.arguments()));
        root.addChild(create(ctx.SEMI()));
        return root;
    }
