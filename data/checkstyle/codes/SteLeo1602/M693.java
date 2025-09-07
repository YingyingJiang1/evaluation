    @Override
    public void visitToken(DetailAST ast) {

        final List<DetailAST> patternVariableIdents = getPatternVariableIdents(ast);
        final List<DetailAST> reassignedVariableIdents = getReassignedVariableIdents(ast);

        for (DetailAST patternVariableIdent : patternVariableIdents) {
            for (DetailAST assignTokenIdent : reassignedVariableIdents) {
                if (patternVariableIdent.getText().equals(assignTokenIdent.getText())) {

                    log(assignTokenIdent, MSG_KEY, assignTokenIdent.getText());
                    break;
                }

            }
        }
    }
