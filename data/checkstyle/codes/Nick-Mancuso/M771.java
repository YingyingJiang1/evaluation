    @Override
    public DetailAstImpl visitClassBody(JavaLanguageParser.ClassBodyContext ctx) {
        final DetailAstImpl objBlock = createImaginary(TokenTypes.OBJBLOCK);
        processChildren(objBlock, ctx.children);
        return objBlock;
    }
