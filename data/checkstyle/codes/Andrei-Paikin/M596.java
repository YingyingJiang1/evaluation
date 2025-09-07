    private void checkTypeMember(DetailAST ast) {
        if (isSemicolon(ast.getNextSibling())) {
            log(ast.getNextSibling(), MSG_SEMI);
        }
    }
