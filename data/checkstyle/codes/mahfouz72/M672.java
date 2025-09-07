    @Override
    public void leaveToken(DetailAST ast) {
        while (lambdaParameters.peek() != null
                    && ast.equals(lambdaParameters.peek().enclosingLambda)) {

            final Optional<LambdaParameterDetails> unusedLambdaParameter =
                    Optional.ofNullable(lambdaParameters.peek())
                            .filter(parameter -> !parameter.isUsed())
                            .filter(parameter -> !"_".equals(parameter.getName()));

            unusedLambdaParameter.ifPresent(parameter -> {
                log(parameter.getIdentifierAst(),
                        MSG_UNUSED_LAMBDA_PARAMETER,
                        parameter.getName());
            });
            lambdaParameters.pop();
        }
    }
