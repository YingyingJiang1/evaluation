    @Override
    public DetailAstImpl visitTypeDeclaration(JavaLanguageParser.TypeDeclarationContext ctx) {
        final DetailAstImpl typeDeclaration;
        if (ctx.type == null) {
            typeDeclaration = create(ctx.semi.get(0));
            ctx.semi.subList(1, ctx.semi.size())
                    .forEach(semi -> addLastSibling(typeDeclaration, create(semi)));
        }
        else {
            typeDeclaration = visit(ctx.type);
        }
        return typeDeclaration;
    }
