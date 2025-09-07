    private static DetailAST getPrevCaseToken(DetailAST parentStatement) {
        final DetailAST prevCaseToken;
        final DetailAST parentBlock = parentStatement.getParent();
        if (parentBlock.getParent().getPreviousSibling() != null
                && parentBlock.getParent().getPreviousSibling().getType()
                    == TokenTypes.LITERAL_CASE) {
            prevCaseToken = parentBlock.getParent().getPreviousSibling();
        }
        else {
            prevCaseToken = null;
        }
        return prevCaseToken;
    }
