    private static DetailAST getPrevStatementWhenCommentIsUnderCase(DetailAST parentStatement) {
        DetailAST prevStmt = null;
        final DetailAST prevBlock = parentStatement.getPreviousSibling();
        if (prevBlock.getLastChild() != null) {
            DetailAST blockBody = prevBlock.getLastChild().getLastChild();
            if (blockBody.getType() == TokenTypes.SEMI) {
                blockBody = blockBody.getPreviousSibling();
            }
            if (blockBody.getType() == TokenTypes.EXPR) {
                if (isUsingOfObjectReferenceToInvokeMethod(blockBody)) {
                    prevStmt = findStartTokenOfMethodCallChain(blockBody);
                }
                else {
                    prevStmt = blockBody.getFirstChild().getFirstChild();
                }
            }
            else {
                if (blockBody.getType() == TokenTypes.SLIST) {
                    prevStmt = blockBody.getParent().getParent();
                }
                else {
                    prevStmt = blockBody;
                }
            }
            if (isComment(prevStmt)) {
                prevStmt = prevStmt.getNextSibling();
            }
        }
        return prevStmt;
    }
