    @Override
    public DetailAstImpl visitTextBlockLiteral(JavaLanguageParser.TextBlockLiteralContext ctx) {
        final DetailAstImpl textBlockLiteralBegin = create(ctx.TEXT_BLOCK_LITERAL_BEGIN());
        textBlockLiteralBegin.addChild(create(ctx.TEXT_BLOCK_CONTENT()));
        textBlockLiteralBegin.addChild(create(ctx.TEXT_BLOCK_LITERAL_END()));
        return textBlockLiteralBegin;
    }
