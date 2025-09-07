    @Override
    public DetailAstImpl visitAnnotationTypeBody(
            JavaLanguageParser.AnnotationTypeBodyContext ctx) {
        final DetailAstImpl objBlock = createImaginary(TokenTypes.OBJBLOCK);
        processChildren(objBlock, ctx.children);
        return objBlock;
    }
