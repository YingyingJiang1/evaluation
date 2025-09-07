    private void visitLambda(DetailAST lambdaAst) {
        parameterNamesStack.push(parameterNames);
        parameterNames = new HashSet<>();

        DetailAST parameterAst = lambdaAst.findFirstToken(TokenTypes.PARAMETERS);
        if (parameterAst == null) {
            parameterAst = lambdaAst.getFirstChild();
        }
        visitLambdaParameters(parameterAst);
    }
