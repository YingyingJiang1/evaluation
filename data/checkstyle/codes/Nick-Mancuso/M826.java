    @Override
    public DetailAstImpl visitSwitchLabeledThrow(
            JavaLanguageParser.SwitchLabeledThrowContext ctx) {
        final DetailAstImpl switchLabel = visit(ctx.switchLabel());
        addLastSibling(switchLabel, create(ctx.LAMBDA()));
        final DetailAstImpl literalThrow = create(ctx.LITERAL_THROW());
        literalThrow.addChild(visit(ctx.expression()));
        literalThrow.addChild(create(ctx.SEMI()));
        addLastSibling(switchLabel, literalThrow);
        return switchLabel;
    }
