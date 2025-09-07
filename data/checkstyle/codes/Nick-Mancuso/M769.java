    @Override
    public DetailAstImpl visitInterfaceDeclaration(
            JavaLanguageParser.InterfaceDeclarationContext ctx) {
        return createTypeDeclaration(ctx, TokenTypes.INTERFACE_DEF, ctx.mods);
    }
