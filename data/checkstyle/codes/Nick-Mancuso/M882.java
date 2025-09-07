    @Override
    public DetailAstImpl visitRecordComponentPatternList(
            JavaLanguageParser.RecordComponentPatternListContext ctx) {
        final DetailAstImpl recordComponents =
                createImaginary(TokenTypes.RECORD_PATTERN_COMPONENTS);
        processChildren(recordComponents, ctx.children);
        return recordComponents;
    }
