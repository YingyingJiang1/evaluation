    @Override
    public DetailAstImpl visitRecordComponents(JavaLanguageParser.RecordComponentsContext ctx) {
        final DetailAstImpl recordComponents = createImaginary(TokenTypes.RECORD_COMPONENTS);
        processChildren(recordComponents, ctx.children);
        return recordComponents;
    }
