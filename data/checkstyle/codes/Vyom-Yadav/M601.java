    @Override
    public void beginTree(DetailAST root) {
        variables.clear();
        typeDeclarations.clear();
        typeDeclAstToTypeDeclDesc.clear();
        anonInnerAstToTypeDeclDesc.clear();
        anonInnerClassHolders.clear();
        packageName = null;
        depth = 0;
    }
