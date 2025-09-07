    private void checkTypeDefinition(DetailAST ast) {
        if (!ScopeUtil.isOuterMostType(ast) && isSemicolon(ast.getNextSibling())) {
            log(ast.getNextSibling(), MSG_SEMI);
        }
        final DetailAST firstMember =
            ast.findFirstToken(TokenTypes.OBJBLOCK).getFirstChild().getNextSibling();
        if (isSemicolon(firstMember) && !ScopeUtil.isInEnumBlock(firstMember)) {
            log(firstMember, MSG_SEMI);
        }
    }
