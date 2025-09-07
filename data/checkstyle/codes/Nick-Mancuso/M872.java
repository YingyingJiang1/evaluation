    @Override
    public DetailAstImpl visitNonWildcardTypeArguments(
            JavaLanguageParser.NonWildcardTypeArgumentsContext ctx) {
        final DetailAstImpl typeArguments = createImaginary(TokenTypes.TYPE_ARGUMENTS);
        typeArguments.addChild(create(TokenTypes.GENERIC_START, (Token) ctx.LT().getPayload()));
        typeArguments.addChild(visit(ctx.typeArgumentsTypeList()));
        typeArguments.addChild(create(TokenTypes.GENERIC_END, (Token) ctx.GT().getPayload()));
        return typeArguments;
    }
