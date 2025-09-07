    private static int getMethodDefLineStart(DetailAST mainAst) {
        // get first type position
        int lineStart = mainAst.findFirstToken(TokenTypes.IDENT).getLineNo();

        // check if there is a type before the indent
        final DetailAST typeNode = mainAst.findFirstToken(TokenTypes.TYPE);
        if (typeNode != null) {
            lineStart = getFirstLine(typeNode);
        }

        // check if there is a modifier before the type
        for (DetailAST node = mainAst.findFirstToken(TokenTypes.MODIFIERS).getFirstChild();
                node != null;
                node = node.getNextSibling()) {
            // skip annotations as we check them else where as outside the method
            if (node.getType() == TokenTypes.ANNOTATION) {
                continue;
            }

            if (node.getLineNo() < lineStart) {
                lineStart = node.getLineNo();
            }
        }

        return lineStart;
    }
