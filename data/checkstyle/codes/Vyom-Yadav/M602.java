    @Override
    public void visitToken(DetailAST ast) {
        final int type = ast.getType();
        if (type == TokenTypes.DOT) {
            visitDotToken(ast, variables);
        }
        else if (type == TokenTypes.VARIABLE_DEF && !skipUnnamedVariables(ast)) {
            visitVariableDefToken(ast);
        }
        else if (type == TokenTypes.IDENT) {
            visitIdentToken(ast, variables);
        }
        else if (isInsideLocalAnonInnerClass(ast)) {
            visitLocalAnonInnerClass(ast);
        }
        else if (isNonLocalTypeDeclaration(ast)) {
            visitNonLocalTypeDeclarationToken(ast);
        }
        else if (type == TokenTypes.PACKAGE_DEF) {
            packageName = CheckUtil.extractQualifiedName(ast.getFirstChild().getNextSibling());
        }
    }
