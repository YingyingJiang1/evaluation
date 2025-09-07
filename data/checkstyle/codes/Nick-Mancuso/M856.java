    @Override
    public DetailAstImpl visitPostfix(JavaLanguageParser.PostfixContext ctx) {
        final DetailAstImpl postfix;
        if (ctx.postfix.getType() == JavaLanguageLexer.INC) {
            postfix = create(TokenTypes.POST_INC, ctx.postfix);
        }
        else {
            postfix = create(TokenTypes.POST_DEC, ctx.postfix);
        }
        postfix.addChild(visit(ctx.expr()));
        return postfix;
    }
