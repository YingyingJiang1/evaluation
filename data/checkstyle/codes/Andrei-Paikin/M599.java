    private void checkEnumConstant(DetailAST ast) {
        final DetailAST next = ast.getNextSibling();
        if (isSemicolon(next) && isSemicolon(next.getNextSibling())) {
            log(next.getNextSibling(), MSG_SEMI);
        }
    }
