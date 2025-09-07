    @Override
    public DetailAstImpl visitBitShift(JavaLanguageParser.BitShiftContext ctx) {
        final DetailAstImpl shiftOperation;

        // We determine the type of shift operation in the parser, instead of the
        // lexer as in older grammars. This makes it easier to parse type parameters
        // and less than/ greater than operators in general.
        if (ctx.LT().size() == LEFT_SHIFT.length()) {
            shiftOperation = create(TokenTypes.SL, (Token) ctx.LT(0).getPayload());
            shiftOperation.setText(LEFT_SHIFT);
        }
        else if (ctx.GT().size() == UNSIGNED_RIGHT_SHIFT.length()) {
            shiftOperation = create(TokenTypes.BSR, (Token) ctx.GT(0).getPayload());
            shiftOperation.setText(UNSIGNED_RIGHT_SHIFT);
        }
        else {
            shiftOperation = create(TokenTypes.SR, (Token) ctx.GT(0).getPayload());
            shiftOperation.setText(RIGHT_SHIFT);
        }

        shiftOperation.addChild(visit(ctx.expr(0)));
        shiftOperation.addChild(visit(ctx.expr(1)));
        return shiftOperation;
    }
