    private void logViolation(int lineNumber,
                              int asteriskColNumber,
                              int expectedColNumber) {

        log(lineNumber,
            expectedColumnNumberWithoutExpandedTabs,
            MSG_KEY,
            asteriskColNumber,
            expectedColNumber);
    }
