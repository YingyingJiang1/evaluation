    @Override
    public void leaveToken(DetailAST ast) {
        if (ast.getType() == TokenTypes.LITERAL_CATCH) {
            final Optional<CatchParameterDetails> unusedCatchParameter =
                    Optional.ofNullable(catchParameters.peek())
                            .filter(parameter -> !parameter.isUsed())
                            .filter(parameter -> !"_".equals(parameter.getName()));

            unusedCatchParameter.ifPresent(parameter -> {
                log(parameter.getParameterDefinition(),
                        MSG_UNUSED_CATCH_PARAMETER,
                        parameter.getName());
            });
            catchParameters.pop();
        }
    }
