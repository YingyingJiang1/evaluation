    @Override
    public DetailAstImpl visitAnnotation(JavaLanguageParser.AnnotationContext ctx) {
        final DetailAstImpl annotation = createImaginary(TokenTypes.ANNOTATION);
        processChildren(annotation, ctx.children);
        return annotation;
    }
