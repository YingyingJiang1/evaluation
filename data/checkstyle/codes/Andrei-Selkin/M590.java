        public Optional<FinalVariableCandidate> findFinalVariableCandidateForAst(DetailAST ast) {
            Optional<FinalVariableCandidate> result = Optional.empty();
            DetailAST storedVariable = null;
            final Optional<FinalVariableCandidate> candidate =
                Optional.ofNullable(scope.get(ast.getText()));
            if (candidate.isPresent()) {
                storedVariable = candidate.orElseThrow().variableIdent;
            }
            if (storedVariable != null && isSameVariables(storedVariable, ast)) {
                result = candidate;
            }
            return result;
        }
