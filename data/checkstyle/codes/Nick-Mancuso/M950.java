    private void expandTreeTableByPath(DetailAST node) {
        TreePath path = new TreePath(node);
        path = path.pathByAddingChild(node);
        if (!tree.isExpanded(path)) {
            tree.expandPath(path);
        }
        tree.setSelectionPath(path);
    }
