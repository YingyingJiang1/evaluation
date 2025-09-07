    @Override
    public void visitToken(DetailAST ast) {
        final List<DetailAST> caseLabels = getAllCaseLabels(ast);
        final boolean hasNullCaseLabel = caseLabels.stream()
                .anyMatch(MissingNullCaseInSwitchCheck::hasLiteralNull);
        if (!hasNullCaseLabel) {
            final boolean hasPatternCaseLabel = caseLabels.stream()
                .anyMatch(MissingNullCaseInSwitchCheck::hasPatternCaseLabel);
            final boolean hasStringCaseLabel = caseLabels.stream()
                .anyMatch(MissingNullCaseInSwitchCheck::hasStringCaseLabel);
            if (hasPatternCaseLabel || hasStringCaseLabel) {
                log(ast, MSG_KEY);
            }
        }
    }
