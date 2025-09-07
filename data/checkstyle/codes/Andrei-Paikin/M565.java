    private static Optional<DetailAST> findCommentUnder(DetailAST packageDef) {
        return Optional.ofNullable(packageDef.getNextSibling())
            .map(sibling -> sibling.findFirstToken(TokenTypes.MODIFIERS))
            .map(DetailAST::getFirstChild)
            .filter(token -> TokenUtil.isCommentType(token.getType()))
            .filter(comment -> comment.getLineNo() == packageDef.getLineNo() + 1);
    }
