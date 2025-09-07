    public TreeView end(String mark) {
        if (current.isRoot()) {
            throw new IllegalStateException("current node is root.");
        }
        current.markEnd().mark(mark);
        current = current.parent;
        return this;
    }
