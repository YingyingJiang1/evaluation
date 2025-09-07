    private DetailAstImpl createLambdaParameter(JavaLanguageParser.IdContext ctx) {
        final DetailAstImpl ident = visitId(ctx);
        final DetailAstImpl parameter = createImaginary(TokenTypes.PARAMETER_DEF);
        final DetailAstImpl modifiers = createImaginary(TokenTypes.MODIFIERS);
        final DetailAstImpl type = createImaginary(TokenTypes.TYPE);
        parameter.addChild(modifiers);
        parameter.addChild(type);
        parameter.addChild(ident);
        return parameter;
    }
