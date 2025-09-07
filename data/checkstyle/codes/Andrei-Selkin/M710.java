    private static boolean isReturnedVariable(AbstractFrame currentFrame, DetailAST ident) {
        final DetailAST blockFrameNameIdent = currentFrame.getFrameNameIdent();
        final DetailAST definitionToken = blockFrameNameIdent.getParent();
        final DetailAST blockStartToken = definitionToken.findFirstToken(TokenTypes.SLIST);
        final DetailAST blockEndToken = getBlockEndToken(blockFrameNameIdent, blockStartToken);

        final Set<DetailAST> returnsInsideBlock = getAllTokensOfType(definitionToken,
            TokenTypes.LITERAL_RETURN, blockEndToken.getLineNo());

        return returnsInsideBlock.stream()
            .anyMatch(returnToken -> isAstInside(returnToken, ident));
    }
