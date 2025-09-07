    @Override
    public DetailAstImpl visitTypeArgumentsTypeList(
            JavaLanguageParser.TypeArgumentsTypeListContext ctx) {
        final DetailAstImpl firstIdent = visit(ctx.typeType(0));
        final DetailAstImpl firstTypeArgument = createImaginary(TokenTypes.TYPE_ARGUMENT);
        firstTypeArgument.addChild(firstIdent);

        for (int i = 0; i < ctx.COMMA().size(); i++) {
            addLastSibling(firstTypeArgument, create(ctx.COMMA(i)));
            final DetailAstImpl ident = visit(ctx.typeType(i + 1));
            final DetailAstImpl typeArgument = createImaginary(TokenTypes.TYPE_ARGUMENT);
            typeArgument.addChild(ident);
            addLastSibling(firstTypeArgument, typeArgument);
        }
        return firstTypeArgument;
    }
