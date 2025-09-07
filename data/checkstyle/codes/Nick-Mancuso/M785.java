    @Override
    public DetailAstImpl visitWildCardTypeArgument(
            JavaLanguageParser.WildCardTypeArgumentContext ctx) {
        final DetailAstImpl typeArgument = createImaginary(TokenTypes.TYPE_ARGUMENT);
        typeArgument.addChild(visit(ctx.annotations()));
        typeArgument.addChild(create(TokenTypes.WILDCARD_TYPE,
                (Token) ctx.QUESTION().getPayload()));

        if (ctx.upperBound != null) {
            final DetailAstImpl upperBound = create(TokenTypes.TYPE_UPPER_BOUNDS, ctx.upperBound);
            upperBound.addChild(visit(ctx.typeType()));
            typeArgument.addChild(upperBound);
        }
        else if (ctx.lowerBound != null) {
            final DetailAstImpl lowerBound = create(TokenTypes.TYPE_LOWER_BOUNDS, ctx.lowerBound);
            lowerBound.addChild(visit(ctx.typeType()));
            typeArgument.addChild(lowerBound);
        }

        return typeArgument;
    }
