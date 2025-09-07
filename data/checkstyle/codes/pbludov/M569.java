    @Override
    public void visitToken(DetailAST ast) {
        if (isTargetNode(ast)) {
            if (option == WrapOption.NL && isNewLineModeViolation(ast)) {
                log(ast, MSG_LINE_NEW, ast.getText());
            }
            else if (option == WrapOption.EOL && isEndOfLineModeViolation(ast)) {
                log(ast, MSG_LINE_PREVIOUS, ast.getText());
            }
        }
    }
