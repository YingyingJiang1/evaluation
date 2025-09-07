    @Override
    public DetailAstImpl visitEnumBody(JavaLanguageParser.EnumBodyContext ctx) {
        final DetailAstImpl objBlock = createImaginary(TokenTypes.OBJBLOCK);
        processChildren(objBlock, ctx.children);
        return objBlock;
    }
