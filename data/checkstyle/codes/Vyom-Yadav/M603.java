    @Override
    public void leaveToken(DetailAST ast) {
        if (TokenUtil.isOfType(ast, SCOPES)) {
            logViolations(ast, variables);
        }
        else if (ast.getType() == TokenTypes.COMPILATION_UNIT) {
            leaveCompilationUnit();
        }
        else if (isNonLocalTypeDeclaration(ast)) {
            depth--;
            typeDeclarations.pop();
        }
    }
