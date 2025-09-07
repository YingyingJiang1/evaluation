    @Override
    public int getChildCount() {
        // lazy init
        if (childCount == NOT_INITIALIZED) {
            childCount = 0;
            DetailAST child = firstChild;

            while (child != null) {
                childCount += 1;
                child = child.getNextSibling();
            }
        }
        return childCount;
    }
