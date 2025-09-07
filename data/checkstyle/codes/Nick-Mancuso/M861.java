    @Override
    public DetailAstImpl visitTypeCastParameters(
            JavaLanguageParser.TypeCastParametersContext ctx) {
        final DetailAstImpl typeType = visit(ctx.typeType(0));
        for (int i = 0; i < ctx.BAND().size(); i++) {
            addLastSibling(typeType, create(TokenTypes.TYPE_EXTENSION_AND,
                                (Token) ctx.BAND(i).getPayload()));
            addLastSibling(typeType, visit(ctx.typeType(i + 1)));
        }
        return typeType;
    }
