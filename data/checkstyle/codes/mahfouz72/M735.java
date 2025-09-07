    private static boolean hasPatternLabel(DetailAST caseAST) {
        return caseAST.findFirstToken(TokenTypes.PATTERN_VARIABLE_DEF) != null
                || caseAST.findFirstToken(TokenTypes.RECORD_PATTERN_DEF) != null
                || caseAST.findFirstToken(TokenTypes.PATTERN_DEF) != null;
    }
