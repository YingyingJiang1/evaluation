    private void checkUnnamedVariables(DetailAST ast) {
        if (jdkVersion >= JDK_22 && isUnnamedVariable(ast)) {
            checkForRedundantModifier(ast, TokenTypes.FINAL);
        }
    }
