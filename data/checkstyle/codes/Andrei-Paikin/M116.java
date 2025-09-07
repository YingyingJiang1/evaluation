    public static String printBranch(DetailAST node) {
        final String result;
        if (node == null) {
            result = "";
        }
        else {
            result = printBranch(node.getParent())
                + getIndentation(node)
                + getNodeInfo(node)
                + LINE_SEPARATOR;
        }
        return result;
    }
