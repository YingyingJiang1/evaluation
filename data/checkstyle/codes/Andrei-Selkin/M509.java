    private static boolean isInEmptyCaseBlock(DetailAST prevStmt, DetailAST nextStmt) {
        return prevStmt != null
            && nextStmt != null
            && (prevStmt.getType() == TokenTypes.LITERAL_CASE
                || prevStmt.getType() == TokenTypes.CASE_GROUP)
            && (nextStmt.getType() == TokenTypes.LITERAL_CASE
                || nextStmt.getType() == TokenTypes.LITERAL_DEFAULT);
    }
