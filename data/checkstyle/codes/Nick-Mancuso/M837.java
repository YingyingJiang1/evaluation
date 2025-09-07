    @Override
    public DetailAstImpl visitDefaultLabel(JavaLanguageParser.DefaultLabelContext ctx) {
        final DetailAstImpl defaultLabel = create(ctx.LITERAL_DEFAULT());
        if (ctx.COLON() != null) {
            defaultLabel.addChild(create(ctx.COLON()));
        }
        return defaultLabel;
    }
