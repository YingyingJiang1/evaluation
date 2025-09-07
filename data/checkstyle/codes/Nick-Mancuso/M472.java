    private static boolean isSingleLineSwitchMember(DetailAST statement) {
        final boolean result;
        if (isInSwitchRule(statement)) {
            result = isSingleLineSwitchRule(statement);
        }
        else {
            result = isSingleLineCaseGroup(statement);
        }
        return result;
    }
