    @Override
    public DetailAstImpl visitDiamond(JavaLanguageParser.DiamondContext ctx) {
        final DetailAstImpl typeArguments =
                createImaginary(TokenTypes.TYPE_ARGUMENTS);
        typeArguments.addChild(create(TokenTypes.GENERIC_START,
                (Token) ctx.LT().getPayload()));
        typeArguments.addChild(create(TokenTypes.GENERIC_END,
                (Token) ctx.GT().getPayload()));
        return typeArguments;
    }
