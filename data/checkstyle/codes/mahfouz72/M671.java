    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getType() == TokenTypes.LAMBDA) {
            final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);
            if (parameters != null) {
                // we have multiple lambda parameters
                TokenUtil.forEachChild(parameters, TokenTypes.PARAMETER_DEF, parameter -> {
                    final DetailAST identifierAst = parameter.findFirstToken(TokenTypes.IDENT);
                    final LambdaParameterDetails lambdaParameter =
                            new LambdaParameterDetails(ast, identifierAst);
                    lambdaParameters.push(lambdaParameter);
                });
            }
            else if (ast.getChildCount() != 0) {
                // we are not switch rule and have a single parameter
                final LambdaParameterDetails lambdaParameter =
                            new LambdaParameterDetails(ast, ast.findFirstToken(TokenTypes.IDENT));
                lambdaParameters.push(lambdaParameter);
            }
        }
        else if (isLambdaParameterIdentifierCandidate(ast) && !isLeftHandOfAssignment(ast)) {
            // we do not count reassignment as usage
            lambdaParameters.stream()
                    .filter(parameter -> parameter.getName().equals(ast.getText()))
                    .findFirst()
                    .ifPresent(LambdaParameterDetails::registerAsUsed);
        }
    }
