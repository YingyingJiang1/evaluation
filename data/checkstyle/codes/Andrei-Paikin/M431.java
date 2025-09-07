    private static boolean hasJavadocAboveAnnotation(DetailAST ast) {
        final Optional<DetailAST> firstAnnotationChild = Optional.of(ast.getFirstChild())
            .map(DetailAST::getFirstChild)
            .map(DetailAST::getFirstChild);
        boolean result = false;
        if (firstAnnotationChild.isPresent()) {
            for (DetailAST child = firstAnnotationChild.orElseThrow(); child != null;
                 child = child.getNextSibling()) {
                if (isJavadoc(child)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
