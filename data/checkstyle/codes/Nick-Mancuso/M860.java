    @Override
    public DetailAstImpl visitMethodCall(JavaLanguageParser.MethodCallContext ctx) {
        final DetailAstImpl methodCall = create(TokenTypes.METHOD_CALL,
                (Token) ctx.LPAREN().getPayload());
        // We always add an 'ELIST' node
        final DetailAstImpl expressionList = Optional.ofNullable(visit(ctx.expressionList()))
                .orElseGet(() -> createImaginary(TokenTypes.ELIST));

        final DetailAstImpl dot = create(ctx.DOT());
        dot.addChild(visit(ctx.expr()));
        dot.addChild(visit(ctx.id()));
        methodCall.addChild(dot);
        methodCall.addChild(expressionList);
        methodCall.addChild(create((Token) ctx.RPAREN().getPayload()));
        return methodCall;
    }
