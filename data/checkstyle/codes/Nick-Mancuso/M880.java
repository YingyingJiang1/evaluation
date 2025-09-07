    @Override
    public DetailAstImpl visitTypePatternDef(
            JavaLanguageParser.TypePatternDefContext ctx) {
        final DetailAstImpl type = visit(ctx.type);
        final DetailAstImpl patternVariableDef = createImaginary(TokenTypes.PATTERN_VARIABLE_DEF);
        patternVariableDef.addChild(createModifiers(ctx.mods));
        patternVariableDef.addChild(type);
        patternVariableDef.addChild(visit(ctx.id()));
        return patternVariableDef;
    }
