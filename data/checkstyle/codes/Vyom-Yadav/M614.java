    private String getQualifiedTypeDeclarationName(DetailAST typeDeclAst) {
        final String className = typeDeclAst.findFirstToken(TokenTypes.IDENT).getText();
        String outerClassQualifiedName = null;
        if (!typeDeclarations.isEmpty()) {
            outerClassQualifiedName = typeDeclarations.peek().getQualifiedName();
        }
        return CheckUtil
            .getQualifiedTypeDeclarationName(packageName, outerClassQualifiedName, className);
    }
