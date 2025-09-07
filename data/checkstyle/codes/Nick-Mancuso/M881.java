    @Override
    public DetailAstImpl visitRecordPattern(JavaLanguageParser.RecordPatternContext ctx) {
        final DetailAstImpl recordPattern = createImaginary(TokenTypes.RECORD_PATTERN_DEF);
        recordPattern.addChild(createModifiers(ctx.mods));
        processChildren(recordPattern,
                ctx.children.subList(ctx.mods.size(), ctx.children.size()));
        return recordPattern;
    }
