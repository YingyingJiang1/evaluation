    private static DetailAST getRightNode(DetailAST node) {
        DetailAST result;
        if (node.getLastChild() == null) {
            result = node.getNextSibling();
        }
        else {
            final DetailAST rightNode;
            if (node.getType() == TokenTypes.QUESTION) {
                rightNode = node.findFirstToken(TokenTypes.COLON).getPreviousSibling();
            }
            else {
                rightNode = node.getLastChild();
            }
            result = adjustParens(rightNode, DetailAST::getPreviousSibling);
        }

        if (!TokenUtil.isOfType(result, TokenTypes.ARRAY_INIT, TokenTypes.ANNOTATION_ARRAY_INIT)) {
            while (result.getFirstChild() != null) {
                result = result.getFirstChild();
            }
        }
        return result;
    }
