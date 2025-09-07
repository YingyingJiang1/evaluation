    @Override
    public DetailAstImpl visitElementValueArrayInitializer(
            JavaLanguageParser.ElementValueArrayInitializerContext ctx) {
        final DetailAstImpl arrayInit =
                create(TokenTypes.ANNOTATION_ARRAY_INIT, (Token) ctx.LCURLY().getPayload());
        processChildren(arrayInit, ctx.children.subList(1, ctx.children.size()));
        return arrayInit;
    }
