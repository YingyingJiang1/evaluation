    @Override
    public DetailAstImpl visitFloatLiteral(JavaLanguageParser.FloatLiteralContext ctx) {
        final DetailAstImpl floatLiteral;
        if (TokenUtil.isOfType(ctx.start.getType(),
                JavaLanguageLexer.DOUBLE_LITERAL, JavaLanguageLexer.HEX_DOUBLE_LITERAL)) {
            floatLiteral = create(TokenTypes.NUM_DOUBLE, ctx.start);
        }
        else {
            floatLiteral = create(TokenTypes.NUM_FLOAT, ctx.start);
        }
        return floatLiteral;
    }
