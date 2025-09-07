    @Override
    public DetailAstImpl visitSwitchBlockStatementGroup(
            JavaLanguageParser.SwitchBlockStatementGroupContext ctx) {
        final DetailAstImpl caseGroup = createImaginary(TokenTypes.CASE_GROUP);
        processChildren(caseGroup, ctx.switchLabel());
        final DetailAstImpl sList = createImaginary(TokenTypes.SLIST);
        processChildren(sList, ctx.slists);
        caseGroup.addChild(sList);
        return caseGroup;
    }
