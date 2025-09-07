    @Override
    public DetailAstImpl visitCatchParameter(JavaLanguageParser.CatchParameterContext ctx) {
        final DetailAstImpl catchParameterDef = createImaginary(TokenTypes.PARAMETER_DEF);
        catchParameterDef.addChild(createModifiers(ctx.mods));
        // filter mods
        processChildren(catchParameterDef, ctx.children.stream()
                .filter(child -> !(child instanceof JavaLanguageParser.VariableModifierContext))
                .collect(Collectors.toUnmodifiableList()));
        return catchParameterDef;
    }
