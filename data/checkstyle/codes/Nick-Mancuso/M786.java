    @Override
    public DetailAstImpl visitFormalParameters(JavaLanguageParser.FormalParametersContext ctx) {
        final DetailAstImpl lparen = create(ctx.LPAREN());

        // We make a "PARAMETERS" node whether parameters exist or not
        if (ctx.formalParameterList() == null) {
            addLastSibling(lparen, createImaginary(TokenTypes.PARAMETERS));
        }
        else {
            addLastSibling(lparen, visit(ctx.formalParameterList()));
        }
        addLastSibling(lparen, create(ctx.RPAREN()));
        return lparen;
    }
