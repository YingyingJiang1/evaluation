    private void checkResourceVariable(DetailAST currentStatement) {
        if (treatTryResourcesAsStatement) {
            final DetailAST nextNode = currentStatement.getNextSibling();
            if (currentStatement.getPreviousSibling().findFirstToken(TokenTypes.ASSIGN) != null) {
                lastVariableResourceStatementEnd = currentStatement.getLineNo();
            }
            if (nextNode.findFirstToken(TokenTypes.ASSIGN) != null
                && nextNode.getLineNo() == lastVariableResourceStatementEnd) {
                log(currentStatement, MSG_KEY);
            }
        }
    }
