    @Nullable
    private static DetailAST getBraceFromSwitchMember(DetailAST ast) {
        final DetailAST brace;
        final DetailAST parent = ast.getParent();
        if (parent.getType() == TokenTypes.SWITCH_RULE) {
            brace = parent.findFirstToken(TokenTypes.SLIST);
        }
        else {
            brace = getBraceAsFirstChild(ast.getNextSibling());
        }
        return brace;
    }
