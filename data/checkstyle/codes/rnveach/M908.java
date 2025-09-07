    private void clearBranchTokenTypes() {
        DetailAstImpl prevParent = parent;
        while (prevParent != null) {
            prevParent.branchTokenTypes = null;
            prevParent = prevParent.parent;
        }
    }
