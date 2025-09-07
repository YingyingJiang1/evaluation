    private BitSet getBranchTokenTypes() {
        // lazy init
        if (branchTokenTypes == null) {
            branchTokenTypes = new BitSet();
            branchTokenTypes.set(type);

            // add union of all children
            DetailAstImpl child = firstChild;
            while (child != null) {
                final BitSet childTypes = child.getBranchTokenTypes();
                branchTokenTypes.or(childTypes);

                child = child.nextSibling;
            }
        }
        return branchTokenTypes;
    }
