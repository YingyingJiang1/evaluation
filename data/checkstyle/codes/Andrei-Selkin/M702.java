    @Override
    public void beginTree(DetailAST rootAST) {
        scopeStates = new ArrayDeque<>();
        classFieldNames = new HashSet<>();
    }
