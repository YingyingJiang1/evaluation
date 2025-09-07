    private static boolean isAnnotatedWithSafeVarargs(DetailAST methodDef) {
        boolean result = false;
        final List<DetailAST> methodAnnotationsList = getMethodAnnotationsList(methodDef);
        for (DetailAST annotationNode : methodAnnotationsList) {
            if ("SafeVarargs".equals(annotationNode.getLastChild().getText())) {
                result = true;
                break;
            }
        }
        return result;
    }
