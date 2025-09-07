    @Override
    public DetailAstImpl visitArguments(JavaLanguageParser.ArgumentsContext ctx) {
        final DetailAstImpl lparen = create(ctx.LPAREN());

        // We always add an 'ELIST' node
        final DetailAstImpl expressionList = Optional.ofNullable(visit(ctx.expressionList()))
                .orElseGet(() -> createImaginary(TokenTypes.ELIST));
        addLastSibling(lparen, expressionList);
        addLastSibling(lparen, create(ctx.RPAREN()));
        return lparen;
    }
