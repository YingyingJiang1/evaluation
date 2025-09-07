    private void leaveLiteralNew(DetailAST ast) {
        if (ast.findFirstToken(TokenTypes.OBJBLOCK) != null) {
            currentFrame = currentFrame.getParent();
        }
    }
