    @Override
    public DetailAstImpl visitCatchType(JavaLanguageParser.CatchTypeContext ctx) {
        final DetailAstImpl type = createImaginary(TokenTypes.TYPE);
        processChildren(type, ctx.children);
        return type;
    }
