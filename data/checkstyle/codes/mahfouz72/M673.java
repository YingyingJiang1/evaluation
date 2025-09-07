    private static boolean isLambdaParameterIdentifierCandidate(DetailAST identifierAst) {
        // we should ignore the ident if it is in the lambda parameters declaration
        final boolean isLambdaParameterDeclaration =
                identifierAst.getParent().getType() == TokenTypes.LAMBDA
                    || identifierAst.getParent().getType() == TokenTypes.PARAMETER_DEF;

        return !isLambdaParameterDeclaration
                 && (hasValidParentToken(identifierAst) || isMethodInvocation(identifierAst));
    }
