    @Override
    public DetailAstImpl visitEnhancedForControlWithRecordPattern(
            JavaLanguageParser.EnhancedForControlWithRecordPatternContext ctx) {
        final DetailAstImpl recordPattern =
                 visit(ctx.pattern());
        addLastSibling(recordPattern, create(ctx.COLON()));
        addLastSibling(recordPattern, visit(ctx.expression()));
        return recordPattern;
    }
