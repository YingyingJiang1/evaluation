    private void checkVariableDefinition(DetailAST variableDef) {
        if (isSemicolon(variableDef.getLastChild()) && isSemicolon(variableDef.getNextSibling())) {
            log(variableDef.getNextSibling(), MSG_SEMI);
        }
    }
