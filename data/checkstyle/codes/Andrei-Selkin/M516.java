    private static DetailAST findStartTokenOfMethodCallChain(DetailAST root) {
        DetailAST startOfMethodCallChain = root;
        while (startOfMethodCallChain.getFirstChild() != null
                && TokenUtil.areOnSameLine(startOfMethodCallChain.getFirstChild(), root)) {
            startOfMethodCallChain = startOfMethodCallChain.getFirstChild();
        }
        if (startOfMethodCallChain.getFirstChild() != null) {
            startOfMethodCallChain = startOfMethodCallChain.getFirstChild().getNextSibling();
        }
        return startOfMethodCallChain;
    }
