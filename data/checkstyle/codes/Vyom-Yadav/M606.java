    private void addInstanceOrClassVar(DetailAST varDefAst) {
        final DetailAST parentAst = varDefAst.getParent();
        if (isNonLocalTypeDeclaration(parentAst.getParent())
                && !isPrivateInstanceVariable(varDefAst)) {
            final DetailAST ident = varDefAst.findFirstToken(TokenTypes.IDENT);
            final VariableDesc desc = new VariableDesc(ident.getText());
            typeDeclAstToTypeDeclDesc.get(parentAst.getParent()).addInstOrClassVar(desc);
        }
    }
