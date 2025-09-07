    @Override
    public DetailAstImpl visitMultiLambdaParams(JavaLanguageParser.MultiLambdaParamsContext ctx) {
        final DetailAstImpl parameters = createImaginary(TokenTypes.PARAMETERS);
        parameters.addChild(createLambdaParameter(ctx.id(0)));

        for (int i = 0; i < ctx.COMMA().size(); i++) {
            parameters.addChild(create(ctx.COMMA(i)));
            parameters.addChild(createLambdaParameter(ctx.id(i + 1)));
        }
        return parameters;
    }
