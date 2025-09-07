    @Override
    public DetailAstImpl visitPrefix(JavaLanguageParser.PrefixContext ctx) {
        final int tokenType;
        switch (ctx.prefix.getType()) {
            case JavaLanguageLexer.PLUS:
                tokenType = TokenTypes.UNARY_PLUS;
                break;
            case JavaLanguageLexer.MINUS:
                tokenType = TokenTypes.UNARY_MINUS;
                break;
            default:
                tokenType = ctx.prefix.getType();
        }
        final DetailAstImpl prefix = create(tokenType, ctx.prefix);
        prefix.addChild(visit(ctx.expr()));
        return prefix;
    }
