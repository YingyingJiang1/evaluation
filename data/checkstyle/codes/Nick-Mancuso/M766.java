    @Override
    public DetailAstImpl visitTypeBound(JavaLanguageParser.TypeBoundContext ctx) {
        final DetailAstImpl typeBoundType = visit(ctx.typeBoundType(0));
        final Iterator<JavaLanguageParser.TypeBoundTypeContext> typeBoundTypeIterator =
                ctx.typeBoundType().listIterator(1);
        ctx.BAND().forEach(band -> {
            addLastSibling(typeBoundType, create(TokenTypes.TYPE_EXTENSION_AND,
                                (Token) band.getPayload()));
            addLastSibling(typeBoundType, visit(typeBoundTypeIterator.next()));
        });
        return typeBoundType;
    }
