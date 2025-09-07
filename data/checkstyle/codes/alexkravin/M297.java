    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.VARIABLE_DEF:
                if (!isAnonymousClassVariable(ast)) {
                    visitVariableDef(ast);
                }
                break;
            case TokenTypes.IMPORT:
                visitImport(ast);
                break;
            default:
                final String exceptionMsg = "Unexpected token type: " + ast.getText();
                throw new IllegalArgumentException(exceptionMsg);
        }
    }
