    @Override
    public DetailAstImpl visitFormalLambdaParam(JavaLanguageParser.FormalLambdaParamContext ctx) {
        final DetailAstImpl lparen = create(ctx.LPAREN());

        // We add an 'PARAMETERS' node here whether it exists or not
        final DetailAstImpl parameters = Optional.ofNullable(visit(ctx.formalParameterList()))
                .orElseGet(() -> createImaginary(TokenTypes.PARAMETERS));
        addLastSibling(lparen, parameters);
        addLastSibling(lparen, create(ctx.RPAREN()));
        return lparen;
    }
