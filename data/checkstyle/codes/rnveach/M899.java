    public void addPreviousSibling(DetailAST ast) {
        clearBranchTokenTypes();
        clearChildCountCache(parent);
        if (ast != null) {
            // parent is set in setNextSibling or parent.setFirstChild
            final DetailAstImpl previousSiblingNode = previousSibling;
            final DetailAstImpl astImpl = (DetailAstImpl) ast;

            if (previousSiblingNode != null) {
                previousSiblingNode.setNextSibling(astImpl);
            }
            else if (parent != null) {
                parent.setFirstChild(astImpl);
            }

            astImpl.setNextSibling(this);
        }
    }
