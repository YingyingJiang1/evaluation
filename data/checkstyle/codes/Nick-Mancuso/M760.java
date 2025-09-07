    @Override
    public DetailAstImpl visitCompactConstructorDeclaration(
            JavaLanguageParser.CompactConstructorDeclarationContext ctx) {
        final DetailAstImpl compactConstructor = createImaginary(TokenTypes.COMPACT_CTOR_DEF);
        compactConstructor.addChild(createModifiers(ctx.mods));
        compactConstructor.addChild(visit(ctx.id()));
        compactConstructor.addChild(visit(ctx.constructorBlock()));
        return compactConstructor;
    }
