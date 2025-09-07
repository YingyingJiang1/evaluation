    @Override
    public DetailAstImpl visitForInit(JavaLanguageParser.ForInitContext ctx) {
        final DetailAstImpl forInit = createImaginary(TokenTypes.FOR_INIT);
        processChildren(forInit, ctx.children);
        return forInit;
    }
