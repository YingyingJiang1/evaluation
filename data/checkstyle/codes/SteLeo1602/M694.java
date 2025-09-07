    private static List<DetailAST> getPatternVariableIdents(DetailAST ast) {

        final DetailAST outermostPatternVariable =
            ast.findFirstToken(TokenTypes.PATTERN_VARIABLE_DEF);

        final DetailAST recordPatternDef;
        if (ast.getType() == TokenTypes.LITERAL_INSTANCEOF) {
            recordPatternDef = ast.findFirstToken(TokenTypes.RECORD_PATTERN_DEF);
        }
        else {
            recordPatternDef = ast;
        }

        final List<DetailAST> patternVariableIdentsArray = new ArrayList<>();

        if (outermostPatternVariable != null) {
            patternVariableIdentsArray.add(
                outermostPatternVariable.findFirstToken(TokenTypes.IDENT));
        }
        else if (recordPatternDef != null) {
            final DetailAST recordPatternComponents = recordPatternDef
                .findFirstToken(TokenTypes.RECORD_PATTERN_COMPONENTS);

            if (recordPatternComponents != null) {
                for (DetailAST innerPatternVariable = recordPatternComponents.getFirstChild();
                     innerPatternVariable != null;
                     innerPatternVariable = innerPatternVariable.getNextSibling()) {

                    if (innerPatternVariable.getType() == TokenTypes.PATTERN_VARIABLE_DEF) {
                        patternVariableIdentsArray.add(
                            innerPatternVariable.findFirstToken(TokenTypes.IDENT));
                    }
                    else {
                        patternVariableIdentsArray.addAll(
                            getPatternVariableIdents(innerPatternVariable));
                    }

                }
            }

        }
        return patternVariableIdentsArray;
    }
