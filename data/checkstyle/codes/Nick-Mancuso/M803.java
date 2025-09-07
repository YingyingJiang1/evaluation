    @Override
    public DetailAstImpl visitDefaultValue(JavaLanguageParser.DefaultValueContext ctx) {
        final DetailAstImpl defaultValue = create(ctx.LITERAL_DEFAULT());
        defaultValue.addChild(visit(ctx.elementValue()));
        return defaultValue;
    }
