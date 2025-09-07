    private void findMaxCostNode(TraceNode node) {
        if (node instanceof MethodNode && !isRoot(node) && !isRoot(node.parent())) {
            MethodNode aNode = (MethodNode) node;
            if (maxCostNode == null || maxCostNode.getTotalCost() < aNode.getTotalCost()) {
                maxCostNode = aNode;
            }
        }
        if (!isLeaf(node)) {
            List<TraceNode> children = node.getChildren();
            if (children != null) {
                for (TraceNode n: children) {
                    findMaxCostNode(n);
                }
            }
        }
    }
