    private static void clearChildCountCache(DetailAstImpl ast) {
        if (ast != null) {
            ast.childCount = NOT_INITIALIZED;
        }
    }
