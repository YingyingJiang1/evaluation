    private static void checkIdentifierAst(DetailAST identAst, Deque<VariableDesc> variablesStack) {
        for (VariableDesc variableDesc : variablesStack) {
            if (identAst.getText().equals(variableDesc.getName())
                    && !isLeftHandSideValue(identAst)) {
                variableDesc.registerAsUsed();
                break;
            }
        }
    }
