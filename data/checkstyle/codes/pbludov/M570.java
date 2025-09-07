    private static boolean isTargetNode(DetailAST node) {
        final boolean result;
        if (node.getType() == TokenTypes.COLON) {
            result = !isColonFromLabel(node);
        }
        else if (node.getType() == TokenTypes.STAR) {
            // Unlike the import statement, the multiply operator always has children
            result = node.hasChildren();
        }
        else {
            result = true;
        }
        return result;
    }
