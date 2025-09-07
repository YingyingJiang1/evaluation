    @Override
    public DetailAstImpl visitTypeParameters(JavaLanguageParser.TypeParametersContext ctx) {
        final DetailAstImpl typeParameters = createImaginary(TokenTypes.TYPE_PARAMETERS);
        typeParameters.addChild(create(TokenTypes.GENERIC_START, (Token) ctx.LT().getPayload()));
        // Exclude '<' and '>'
        processChildren(typeParameters, ctx.children.subList(1, ctx.children.size() - 1));
        typeParameters.addChild(create(TokenTypes.GENERIC_END, (Token) ctx.GT().getPayload()));
        return typeParameters;
    }
