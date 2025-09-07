    @Override
    public DetailAstImpl visitRecordComponent(JavaLanguageParser.RecordComponentContext ctx) {
        final DetailAstImpl recordComponent = createImaginary(TokenTypes.RECORD_COMPONENT_DEF);
        processChildren(recordComponent, ctx.children);
        return recordComponent;
    }
