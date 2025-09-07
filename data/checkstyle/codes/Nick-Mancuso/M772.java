    @Override
    public DetailAstImpl visitInterfaceBody(JavaLanguageParser.InterfaceBodyContext ctx) {
        final DetailAstImpl objBlock = createImaginary(TokenTypes.OBJBLOCK);
        processChildren(objBlock, ctx.children);
        return objBlock;
    }
