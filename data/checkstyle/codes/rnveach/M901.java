    private void setParent(DetailAstImpl parent) {
        DetailAstImpl instance = this;
        do {
            instance.clearBranchTokenTypes();
            instance.parent = parent;
            instance = instance.nextSibling;
        } while (instance != null);
    }
