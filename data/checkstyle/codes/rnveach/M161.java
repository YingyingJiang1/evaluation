    @Override
    public int compareOrder(NodeInfo other) {
        int result = 0;
        if (other instanceof AbstractNode) {
            result = Integer.compare(depth, ((AbstractNode) other).getDepth());
            if (result == 0) {
                result = compareCommonAncestorChildrenOrder(this, other);
            }
        }
        return result;
    }
