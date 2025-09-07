    @Override
    public DetailAstImpl visitAnnotationTypeElementDeclaration(
            JavaLanguageParser.AnnotationTypeElementDeclarationContext ctx) {
        final DetailAstImpl returnTree;
        if (ctx.SEMI() == null) {
            returnTree = visit(ctx.annotationTypeElementRest());
        }
        else {
            returnTree = create(ctx.SEMI());
        }
        return returnTree;
    }
