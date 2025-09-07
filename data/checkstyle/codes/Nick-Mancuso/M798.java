    @Override
    public DetailAstImpl visitAnnotationTypeDeclaration(
            JavaLanguageParser.AnnotationTypeDeclarationContext ctx) {
        return createTypeDeclaration(ctx, TokenTypes.ANNOTATION_DEF, ctx.mods);
    }
