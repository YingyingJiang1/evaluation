    private void modifyVariablesStack(TypeDeclDesc obtainedClass,
            Deque<VariableDesc> variablesStack,
            DetailAST literalNewAst) {
        if (obtainedClass != null) {
            final Deque<VariableDesc> instAndClassVarDeque = typeDeclAstToTypeDeclDesc
                    .get(obtainedClass.getTypeDeclAst())
                    .getUpdatedCopyOfVarStack(literalNewAst);
            instAndClassVarDeque.forEach(variablesStack::push);
        }
    }
