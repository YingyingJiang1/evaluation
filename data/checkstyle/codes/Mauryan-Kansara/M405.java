    @Override
    public void visitJavadocToken(DetailNode ast) {
        // this method checks the alignment of leading asterisks.
        final boolean isJavadocStartingLine = ast.getLineNumber() == javadocStartLineNumber;

        if (!isJavadocStartingLine) {
            final Optional<Integer> leadingAsteriskColumnNumber =
                                        getAsteriskColumnNumber(ast.getText());

            leadingAsteriskColumnNumber
                    .map(columnNumber -> expandedTabs(ast.getText(), columnNumber))
                    .filter(columnNumber -> {
                        return !hasValidAlignment(expectedColumnNumberTabsExpanded, columnNumber);
                    })
                    .ifPresent(columnNumber -> {
                        logViolation(ast.getLineNumber(),
                                columnNumber,
                                expectedColumnNumberTabsExpanded);
                    });
        }
    }
