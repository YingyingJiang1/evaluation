    @Override
    public DetailAstImpl visitTypeArguments(JavaLanguageParser.TypeArgumentsContext ctx) {
        final DetailAstImpl typeArguments = createImaginary(TokenTypes.TYPE_ARGUMENTS);
        typeArguments.addChild(create(TokenTypes.GENERIC_START, (Token) ctx.LT().getPayload()));
        // Exclude '<' and '>'
        processChildren(typeArguments, ctx.children.subList(1, ctx.children.size() - 1));
        typeArguments.addChild(create(TokenTypes.GENERIC_END, (Token) ctx.GT().getPayload()));
        return typeArguments;
    }
