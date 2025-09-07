    private DetailAST findPreviousStatement(DetailAST comment, DetailAST root) {
        DetailAST previousStatement = null;
        if (root.getLineNo() >= comment.getLineNo()) {
            // ATTENTION: parent of the comment is below the comment in case block
            // See https://github.com/checkstyle/checkstyle/issues/851
            previousStatement = getPrevStatementFromSwitchBlock(comment);
        }
        final DetailAST tokenWhichBeginsTheLine;
        if (root.getType() == TokenTypes.EXPR
                && root.getFirstChild().getFirstChild() != null) {
            if (root.getFirstChild().getType() == TokenTypes.LITERAL_NEW) {
                tokenWhichBeginsTheLine = root.getFirstChild();
            }
            else {
                tokenWhichBeginsTheLine = findTokenWhichBeginsTheLine(root);
            }
        }
        else if (root.getType() == TokenTypes.PLUS) {
            tokenWhichBeginsTheLine = root.getFirstChild();
        }
        else {
            tokenWhichBeginsTheLine = root;
        }
        if (tokenWhichBeginsTheLine != null
                && !isComment(tokenWhichBeginsTheLine)
                && isOnPreviousLineIgnoringComments(comment, tokenWhichBeginsTheLine)) {
            previousStatement = tokenWhichBeginsTheLine;
        }
        return previousStatement;
    }
