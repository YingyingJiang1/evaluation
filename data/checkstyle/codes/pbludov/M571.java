    private static DetailAST getLeftNode(DetailAST node) {
        DetailAST result;
        if (node.getFirstChild() == null || isAssignToVariable(node)) {
            result = node.getPreviousSibling();
        }
        else if (isInPatternDefinition(node)) {
            result = node.getFirstChild();
        }
        else {
            result = adjustParens(node.getFirstChild(), DetailAST::getNextSibling);
        }
        while (result.getLastChild() != null) {
            result = result.getLastChild();
        }
        return result;
    }
