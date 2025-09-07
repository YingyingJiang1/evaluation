    private static boolean hasJavadoc(DetailAST ast) {
        final boolean hasJavadocBefore = ast.getPreviousSibling() != null
            && isJavadoc(ast.getPreviousSibling());
        return hasJavadocBefore || hasJavadocAboveAnnotation(ast);
    }
