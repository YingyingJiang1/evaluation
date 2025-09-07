    public static DetailAST createBlockCommentNode(String content) {
        final DetailAstImpl blockCommentBegin = new DetailAstImpl();
        blockCommentBegin.setType(TokenTypes.BLOCK_COMMENT_BEGIN);
        blockCommentBegin.setText(BLOCK_MULTIPLE_COMMENT_BEGIN);
        blockCommentBegin.setLineNo(0);
        blockCommentBegin.setColumnNo(-JAVADOC_START.length());

        final DetailAstImpl commentContent = new DetailAstImpl();
        commentContent.setType(TokenTypes.COMMENT_CONTENT);
        commentContent.setText("*" + content);
        commentContent.setLineNo(0);
        // javadoc should starts at 0 column, so COMMENT_CONTENT node
        // that contains javadoc identifier has -1 column
        commentContent.setColumnNo(-1);

        final DetailAstImpl blockCommentEnd = new DetailAstImpl();
        blockCommentEnd.setType(TokenTypes.BLOCK_COMMENT_END);
        blockCommentEnd.setText(BLOCK_MULTIPLE_COMMENT_END);

        blockCommentBegin.setFirstChild(commentContent);
        commentContent.setNextSibling(blockCommentEnd);
        return blockCommentBegin;
    }
