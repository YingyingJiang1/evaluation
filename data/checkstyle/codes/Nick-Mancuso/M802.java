    @Override
    public DetailAstImpl visitAnnotationMethodRest(
            JavaLanguageParser.AnnotationMethodRestContext ctx) {
        final DetailAstImpl annotationFieldDef =
                createImaginary(TokenTypes.ANNOTATION_FIELD_DEF);
        annotationFieldDef.addChild(createModifiers(ctx.mods));
        annotationFieldDef.addChild(visit(ctx.type));

        // Process all children except C style array declarators
        processChildren(annotationFieldDef, ctx.children.stream()
                .filter(child -> !(child instanceof JavaLanguageParser.ArrayDeclaratorContext))
                .collect(Collectors.toUnmodifiableList()));

        // We add C style array declarator brackets to TYPE ast
        final DetailAstImpl typeAst =
                (DetailAstImpl) annotationFieldDef.findFirstToken(TokenTypes.TYPE);
        ctx.cStyleArrDec.forEach(child -> typeAst.addChild(visit(child)));

        return annotationFieldDef;
    }
