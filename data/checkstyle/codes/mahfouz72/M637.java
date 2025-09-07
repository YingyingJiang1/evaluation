    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getType() == TokenTypes.LITERAL_CATCH) {
            final CatchParameterDetails catchParameter = new CatchParameterDetails(ast);
            catchParameters.push(catchParameter);
        }
        else if (isCatchParameterIdentifierCandidate(ast) && !isLeftHandOfAssignment(ast)) {
            // we do not count reassignment as usage
            catchParameters.stream()
                    .filter(parameter -> parameter.getName().equals(ast.getText()))
                    .findFirst()
                    .ifPresent(CatchParameterDetails::registerAsUsed);
        }
    }
