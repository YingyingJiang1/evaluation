    @Override
    public DetailAstImpl visitSimpleMethodCall(JavaLanguageParser.SimpleMethodCallContext ctx) {
        final DetailAstImpl methodCall = create(TokenTypes.METHOD_CALL,
                (Token) ctx.LPAREN().getPayload());
        methodCall.addChild(visit(ctx.id()));
        // We always add an 'ELIST' node
        final DetailAstImpl expressionList = Optional.ofNullable(visit(ctx.expressionList()))
                .orElseGet(() -> createImaginary(TokenTypes.ELIST));

        methodCall.addChild(expressionList);
        methodCall.addChild(create((Token) ctx.RPAREN().getPayload()));
        return methodCall;
    }
