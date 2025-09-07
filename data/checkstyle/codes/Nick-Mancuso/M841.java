    @Override
    public DetailAstImpl visitEnhancedForControl(
            JavaLanguageParser.EnhancedForControlContext ctx) {
        final DetailAstImpl variableDeclaratorId =
                 visit(ctx.variableDeclaratorId());
        final DetailAstImpl variableDef = createImaginary(TokenTypes.VARIABLE_DEF);
        variableDef.addChild(variableDeclaratorId);

        addLastSibling(variableDef, create(ctx.COLON()));
        addLastSibling(variableDef, visit(ctx.expression()));
        return variableDef;
    }
