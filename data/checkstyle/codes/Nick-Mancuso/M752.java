    @Override
    public DetailAstImpl visitPackageDeclaration(
            JavaLanguageParser.PackageDeclarationContext ctx) {
        final DetailAstImpl packageDeclaration =
                create(TokenTypes.PACKAGE_DEF, (Token) ctx.LITERAL_PACKAGE().getPayload());
        packageDeclaration.addChild(visit(ctx.annotations()));
        packageDeclaration.addChild(visit(ctx.qualifiedName()));
        packageDeclaration.addChild(create(ctx.SEMI()));
        return packageDeclaration;
    }
