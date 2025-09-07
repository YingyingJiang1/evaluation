    @Override
    public DetailAstImpl visitSwitchBlocks(JavaLanguageParser.SwitchBlocksContext ctx) {
        final DetailAstImpl dummyRoot = new DetailAstImpl();
        ctx.groups.forEach(group -> dummyRoot.addChild(visit(group)));

        // Add any empty switch labels to end of statement in one 'CASE_GROUP'
        if (!ctx.emptyLabels.isEmpty()) {
            final DetailAstImpl emptyLabelParent =
                    createImaginary(TokenTypes.CASE_GROUP);
            ctx.emptyLabels.forEach(label -> emptyLabelParent.addChild(visit(label)));
            dummyRoot.addChild(emptyLabelParent);
        }
        return dummyRoot.getFirstChild();
    }
