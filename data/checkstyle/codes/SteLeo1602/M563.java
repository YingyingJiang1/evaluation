    private boolean isTwoPrecedingPreviousLinesFromCommentEmpty(DetailAST token) {
        boolean upToPrePreviousLinesEmpty = false;

        for (DetailAST typeChild = token.findFirstToken(TokenTypes.TYPE).getLastChild();
             typeChild != null; typeChild = typeChild.getPreviousSibling()) {

            if (isTokenNotOnPreviousSiblingLines(typeChild, token)) {

                final String commentBeginningPreviousLine =
                    getLine(typeChild.getLineNo() - 2);
                final String commentBeginningPrePreviousLine =
                    getLine(typeChild.getLineNo() - 3);

                if (CommonUtil.isBlank(commentBeginningPreviousLine)
                    && CommonUtil.isBlank(commentBeginningPrePreviousLine)) {
                    upToPrePreviousLinesEmpty = true;
                    break;
                }

            }

        }

        return upToPrePreviousLinesEmpty;
    }
