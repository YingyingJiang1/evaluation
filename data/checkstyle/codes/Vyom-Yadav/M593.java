    private void visitLambdaParameters(DetailAST ast) {
        if (ast.getType() == TokenTypes.IDENT) {
            parameterNames.add(ast.getText());
        }
        else {
            visitParameters(ast);
        }
    }
