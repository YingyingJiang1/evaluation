    @Override
    public DetailAstImpl visitSimpleTypeArgument(
            JavaLanguageParser.SimpleTypeArgumentContext ctx) {
        final DetailAstImpl typeArgument =
                createImaginary(TokenTypes.TYPE_ARGUMENT);
        typeArgument.addChild(visit(ctx.typeType()));
        return typeArgument;
    }
