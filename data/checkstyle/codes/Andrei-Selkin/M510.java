    private static boolean isFallThroughComment(DetailAST prevStmt, DetailAST nextStmt) {
        return prevStmt != null
            && nextStmt != null
            && prevStmt.getType() != TokenTypes.LITERAL_CASE
            && (nextStmt.getType() == TokenTypes.LITERAL_CASE
                || nextStmt.getType() == TokenTypes.LITERAL_DEFAULT);
    }
