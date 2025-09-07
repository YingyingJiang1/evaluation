    private static boolean isTokenNotOnPreviousSiblingLines(DetailAST token,
                                                            DetailAST parentToken) {
        DetailAST previousSibling = parentToken.getPreviousSibling();
        for (DetailAST astNode = previousSibling; astNode != null;
             astNode = astNode.getLastChild()) {
            previousSibling = astNode;
        }

        return token.getLineNo() != previousSibling.getLineNo();
    }
