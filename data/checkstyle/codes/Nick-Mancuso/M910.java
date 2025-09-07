    public void setFirstChild(DetailAST firstChild) {
        clearBranchTokenTypes();
        clearChildCountCache(this);
        this.firstChild = (DetailAstImpl) firstChild;
        if (firstChild != null) {
            ((DetailAstImpl) firstChild).setParent(this);
        }
    }
