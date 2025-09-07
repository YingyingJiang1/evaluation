    @Override
    public DetailAstImpl visitLastRecordComponent(
            JavaLanguageParser.LastRecordComponentContext ctx) {
        final DetailAstImpl recordComponent = createImaginary(TokenTypes.RECORD_COMPONENT_DEF);
        processChildren(recordComponent, ctx.children);
        return recordComponent;
    }
