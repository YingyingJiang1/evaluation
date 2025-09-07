    @Override
    public DetailAstImpl visitFieldDeclaration(JavaLanguageParser.FieldDeclarationContext ctx) {
        final DetailAstImpl dummyNode = new DetailAstImpl();
        // Since the TYPE AST is built by visitVariableDeclarator(), we skip it here (child [0])
        // We also append the SEMI token to the first child [size() - 1],
        // until https://github.com/checkstyle/checkstyle/issues/3151
        processChildren(dummyNode, ctx.children.subList(1, ctx.children.size() - 1));
        dummyNode.getFirstChild().addChild(create(ctx.SEMI()));
        return dummyNode.getFirstChild();
    }
