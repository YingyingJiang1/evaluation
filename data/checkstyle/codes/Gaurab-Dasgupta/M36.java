    private static int getParentIndexOf(DetailNode node) {
        DetailNode currNode = node;
        while (currNode.getParent().getIndex() != -1) {
            currNode = currNode.getParent();
        }
        return currNode.getIndex();
    }
