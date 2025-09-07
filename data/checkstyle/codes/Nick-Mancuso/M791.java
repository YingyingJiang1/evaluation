    @Override
    public DetailAstImpl visitIntegerLiteral(JavaLanguageParser.IntegerLiteralContext ctx) {
        final int[] longTypes = {
            JavaLanguageLexer.DECIMAL_LITERAL_LONG,
            JavaLanguageLexer.HEX_LITERAL_LONG,
            JavaLanguageLexer.OCT_LITERAL_LONG,
            JavaLanguageLexer.BINARY_LITERAL_LONG,
        };

        final int tokenType;
        if (TokenUtil.isOfType(ctx.start.getType(), longTypes)) {
            tokenType = TokenTypes.NUM_LONG;
        }
        else {
            tokenType = TokenTypes.NUM_INT;
        }

        return create(tokenType, ctx.start);
    }
