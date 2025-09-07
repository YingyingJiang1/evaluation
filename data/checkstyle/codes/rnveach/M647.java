    @Override
    public void beginTree(DetailAST rootAST) {
        illegalShortClassNames.clear();
        illegalShortClassNames.addAll(illegalClassNames);
    }
