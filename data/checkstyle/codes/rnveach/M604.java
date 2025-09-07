    private void visitNonLocalTypeDeclarationToken(DetailAST typeDeclAst) {
        final String qualifiedName = getQualifiedTypeDeclarationName(typeDeclAst);
        final TypeDeclDesc currTypeDecl = new TypeDeclDesc(qualifiedName, depth, typeDeclAst);
        depth++;
        typeDeclarations.push(currTypeDecl);
        typeDeclAstToTypeDeclDesc.put(typeDeclAst, currTypeDecl);
    }
