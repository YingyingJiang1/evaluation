    private void customVisitToken(DetailAST ast, Deque<VariableDesc> variablesStack) {
        final int type = ast.getType();
        if (type == TokenTypes.DOT) {
            visitDotToken(ast, variablesStack);
        }
        else if (type == TokenTypes.VARIABLE_DEF) {
            addLocalVariables(ast, variablesStack);
        }
        else if (type == TokenTypes.IDENT) {
            visitIdentToken(ast, variablesStack);
        }
        else if (isInsideLocalAnonInnerClass(ast)) {
            final TypeDeclDesc obtainedClass = getSuperClassOfAnonInnerClass(ast);
            modifyVariablesStack(obtainedClass, variablesStack, ast);
        }
    }
