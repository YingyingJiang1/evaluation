    @Override
    public DetailAstImpl visitResources(JavaLanguageParser.ResourcesContext ctx) {
        final DetailAstImpl firstResource = visit(ctx.resource(0));
        final DetailAstImpl resources = createImaginary(TokenTypes.RESOURCES);
        resources.addChild(firstResource);
        processChildren(resources, ctx.children.subList(1, ctx.children.size()));
        return resources;
    }
