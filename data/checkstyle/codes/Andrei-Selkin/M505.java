    @Override
    public void visitToken(DetailAST commentAst) {
        switch (commentAst.getType()) {
            case TokenTypes.SINGLE_LINE_COMMENT:
            case TokenTypes.BLOCK_COMMENT_BEGIN:
                visitComment(commentAst);
                break;
            default:
                final String exceptionMsg = "Unexpected token type: " + commentAst.getText();
                throw new IllegalArgumentException(exceptionMsg);
        }
    }
