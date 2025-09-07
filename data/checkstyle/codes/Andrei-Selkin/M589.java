    private Optional<FinalVariableCandidate> getFinalCandidate(DetailAST ast) {
        Optional<FinalVariableCandidate> result = Optional.empty();
        final Iterator<ScopeData> iterator = scopeStack.descendingIterator();
        while (iterator.hasNext() && result.isEmpty()) {
            final ScopeData scopeData = iterator.next();
            result = scopeData.findFinalVariableCandidateForAst(ast);
        }
        return result;
    }
