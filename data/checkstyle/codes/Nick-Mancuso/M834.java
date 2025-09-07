    @Override
    public DetailAstImpl visitResourceDeclaration(
            JavaLanguageParser.ResourceDeclarationContext ctx) {
        final DetailAstImpl resource = createImaginary(TokenTypes.RESOURCE);
        resource.addChild(visit(ctx.variableDeclaratorId()));

        final DetailAstImpl assign = create(ctx.ASSIGN());
        resource.addChild(assign);
        assign.addChild(visit(ctx.expression()));
        return resource;
    }
