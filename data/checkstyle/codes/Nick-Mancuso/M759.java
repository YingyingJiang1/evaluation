    @Override
    public DetailAstImpl visitRecordBody(JavaLanguageParser.RecordBodyContext ctx) {
        final DetailAstImpl objBlock = createImaginary(TokenTypes.OBJBLOCK);
        processChildren(objBlock, ctx.children);
        return objBlock;
    }
