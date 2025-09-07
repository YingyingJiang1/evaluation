    @Override
    public DetailAstImpl visitConstructorDeclaration(
            JavaLanguageParser.ConstructorDeclarationContext ctx) {
        final DetailAstImpl constructorDeclaration = createImaginary(TokenTypes.CTOR_DEF);
        constructorDeclaration.addChild(createModifiers(ctx.mods));
        processChildren(constructorDeclaration, ctx.children);
        return constructorDeclaration;
    }
