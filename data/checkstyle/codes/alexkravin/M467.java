    private static boolean isEmptyCatchBlock(DetailAST catchAst) {
        boolean result = true;
        final DetailAST slistToken = catchAst.findFirstToken(TokenTypes.SLIST);
        DetailAST catchBlockStmt = slistToken.getFirstChild();
        while (catchBlockStmt.getType() != TokenTypes.RCURLY) {
            if (catchBlockStmt.getType() != TokenTypes.SINGLE_LINE_COMMENT
                 && catchBlockStmt.getType() != TokenTypes.BLOCK_COMMENT_BEGIN) {
                result = false;
                break;
            }
            catchBlockStmt = catchBlockStmt.getNextSibling();
        }
        return result;
    }
