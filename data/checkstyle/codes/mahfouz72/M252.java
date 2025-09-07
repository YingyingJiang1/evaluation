    private void processLambdaParameters(DetailAST lambdaAst) {
        final DetailAST lambdaParameters = lambdaAst.findFirstToken(TokenTypes.PARAMETERS);
        if (lambdaParameters != null) {
            TokenUtil.forEachChild(lambdaParameters, TokenTypes.PARAMETER_DEF,
                    this::checkUnnamedVariables);
        }
    }
