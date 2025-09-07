    private void checkThrows() {
        final DetailAST throwsAst = getMainAst().findFirstToken(TokenTypes.LITERAL_THROWS);

        if (throwsAst != null) {
            checkWrappingIndentation(throwsAst, throwsAst.getNextSibling(), getIndentCheck()
                    .getThrowsIndent(), getLineStart(getMethodDefLineStart(getMainAst())),
                    !isOnStartOfLine(throwsAst));
        }
    }
