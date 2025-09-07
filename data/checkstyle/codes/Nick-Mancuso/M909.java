    public void setNextSibling(DetailAST nextSibling) {
        clearBranchTokenTypes();
        clearChildCountCache(parent);
        this.nextSibling = (DetailAstImpl) nextSibling;
        if (nextSibling != null && parent != null) {
            ((DetailAstImpl) nextSibling).setParent(parent);
        }
        if (nextSibling != null) {
            ((DetailAstImpl) nextSibling).previousSibling = this;
        }
    }
