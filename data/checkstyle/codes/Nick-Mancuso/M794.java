    @Override
    public DetailAstImpl visitAnnotations(JavaLanguageParser.AnnotationsContext ctx) {
        final DetailAstImpl annotations;

        if (!ctx.createImaginaryNode && ctx.anno.isEmpty()) {
            // There are no annotations, and we don't want to create the empty node
            annotations = null;
        }
        else {
            // There are annotations, or we just want the empty node
            annotations = createImaginary(TokenTypes.ANNOTATIONS);
            processChildren(annotations, ctx.anno);
        }

        return annotations;
    }
