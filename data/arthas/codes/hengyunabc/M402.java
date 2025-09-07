    private void findMaxCostNode(Node node) {
        if (!node.isRoot() && !node.parent.isRoot()) {
            if (maxCost == null) {
                maxCost = node;
            } else if (maxCost.totalCost < node.totalCost) {
                maxCost = node;
            }
        }
        if (!node.isLeaf()) {
            for (Node n: node.children) {
                findMaxCostNode(n);
            }
        }
    }
