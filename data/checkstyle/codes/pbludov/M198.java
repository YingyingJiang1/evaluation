    public static List<AbstractNode> createChildren(AbstractNode root, AbstractNode parent,
                                                    DetailAST firstChild) {
        DetailAST currentChild = firstChild;
        final int depth = parent.getDepth() + 1;
        final List<AbstractNode> result = new ArrayList<>();
        while (currentChild != null) {
            final int index = result.size();
            final ElementNode child = new ElementNode(root, parent, currentChild, depth, index);
            result.add(child);
            currentChild = currentChild.getNextSibling();
        }
        return result;
    }
