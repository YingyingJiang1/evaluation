    private static int compareCommonAncestorChildrenOrder(NodeInfo first, NodeInfo second) {
        NodeInfo child1 = first;
        NodeInfo child2 = second;
        while (!child1.getParent().equals(child2.getParent())) {
            child1 = child1.getParent();
            child2 = child2.getParent();
        }
        final int index1 = ((AbstractElementNode) child1).indexAmongSiblings;
        final int index2 = ((AbstractElementNode) child2).indexAmongSiblings;
        return Integer.compare(index1, index2);
    }
