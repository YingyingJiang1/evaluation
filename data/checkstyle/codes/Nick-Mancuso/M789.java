    @Override
    public DetailAstImpl visitLastFormalParameter(
            JavaLanguageParser.LastFormalParameterContext ctx) {
        final DetailAstImpl parameterDef =
                createImaginary(TokenTypes.PARAMETER_DEF);
        parameterDef.addChild(visit(ctx.variableDeclaratorId()));
        final DetailAstImpl ident = (DetailAstImpl) parameterDef.findFirstToken(TokenTypes.IDENT);
        ident.addPreviousSibling(create(ctx.ELLIPSIS()));
        // We attach annotations on ellipses in varargs to the 'TYPE' ast
        final DetailAstImpl type = (DetailAstImpl) parameterDef.findFirstToken(TokenTypes.TYPE);
        type.addChild(visit(ctx.annotations()));
        return parameterDef;
    }
