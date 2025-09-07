    private static boolean isAstInside(DetailAST tree, DetailAST ast) {
        boolean result = false;

        if (isAstSimilar(tree, ast)) {
            result = true;
        }
        else {
            for (DetailAST child = tree.getFirstChild(); child != null
                    && !result; child = child.getNextSibling()) {
                result = isAstInside(child, ast);
            }
        }

        return result;
    }
