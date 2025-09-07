    @Override
    public DetailAstImpl visitCaseLabel(JavaLanguageParser.CaseLabelContext ctx) {
        final DetailAstImpl caseLabel = create(ctx.LITERAL_CASE());
        // child [0] is 'LITERAL_CASE'
        processChildren(caseLabel, ctx.children.subList(1, ctx.children.size()));
        return caseLabel;
    }
