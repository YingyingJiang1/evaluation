    public static boolean isOnPackage(DetailAST blockComment) {
        boolean result = isOnTokenWithAnnotation(blockComment, TokenTypes.PACKAGE_DEF);

        if (!result) {
            DetailAST nextSibling = blockComment.getNextSibling();

            while (nextSibling != null
                    && nextSibling.getType() == TokenTypes.SINGLE_LINE_COMMENT) {
                nextSibling = nextSibling.getNextSibling();
            }

            result = nextSibling != null && nextSibling.getType() == TokenTypes.PACKAGE_DEF;
        }

        return result;
    }
