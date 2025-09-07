    private DetailAstImpl flattenedTree(ParserRuleContext ctx) {
        final DetailAstImpl dummyNode = new DetailAstImpl();
        processChildren(dummyNode, ctx.children);
        return dummyNode.getFirstChild();
    }
