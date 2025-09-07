    private boolean hasReliefComment(DetailAST ast) {
        final DetailAST nonCommentAst = getNextNonCommentAst(ast);
        boolean result = false;
        if (nonCommentAst != null) {
            final int prevLineNumber = nonCommentAst.getPreviousSibling().getLineNo();
            result = Stream.iterate(nonCommentAst.getPreviousSibling(),
                            Objects::nonNull,
                            DetailAST::getPreviousSibling)
                    .takeWhile(sibling -> sibling.getLineNo() == prevLineNumber)
                    .map(DetailAST::getFirstChild)
                    .filter(Objects::nonNull)
                    .anyMatch(firstChild -> reliefPattern.matcher(firstChild.getText()).find());
        }
        return result;
    }
