    @Override
    public DetailAstImpl visitSwitchRules(JavaLanguageParser.SwitchRulesContext ctx) {
        final DetailAstImpl dummyRoot = new DetailAstImpl();
        ctx.switchLabeledRule().forEach(switchLabeledRuleContext -> {
            final DetailAstImpl switchRule = visit(switchLabeledRuleContext);
            final DetailAstImpl switchRuleParent = createImaginary(TokenTypes.SWITCH_RULE);
            switchRuleParent.addChild(switchRule);
            dummyRoot.addChild(switchRuleParent);
        });
        return dummyRoot.getFirstChild();
    }
