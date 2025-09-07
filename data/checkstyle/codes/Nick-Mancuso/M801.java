    @Override
    public DetailAstImpl visitAnnotationField(JavaLanguageParser.AnnotationFieldContext ctx) {
        final DetailAstImpl dummyNode = new DetailAstImpl();
        // Since the TYPE AST is built by visitAnnotationMethodOrConstantRest(), we skip it
        // here (child [0])
        processChildren(dummyNode, Collections.singletonList(ctx.children.get(1)));
        // We also append the SEMI token to the first child [size() - 1],
        // until https://github.com/checkstyle/checkstyle/issues/3151
        dummyNode.getFirstChild().addChild(create(ctx.SEMI()));
        return dummyNode.getFirstChild();
    }
