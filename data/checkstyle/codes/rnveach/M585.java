    @Override
    public void beginTree(DetailAST rootAST) {
        if (rootAST != null) {
            visitEachToken(rootAST);
        }
    }
