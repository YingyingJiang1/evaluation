    private static boolean hasEmptyImplementation(DetailAST ast) {
        boolean hasEmptyBody = true;
        final DetailAST methodImplOpenBrace = ast.findFirstToken(TokenTypes.SLIST);
        final DetailAST methodImplCloseBrace = methodImplOpenBrace.getLastChild();
        final Predicate<DetailAST> predicate = currentNode -> {
            return currentNode != methodImplCloseBrace
                && !TokenUtil.isCommentType(currentNode.getType());
        };
        final Optional<DetailAST> methodBody =
            TokenUtil.findFirstTokenByPredicate(methodImplOpenBrace, predicate);
        if (methodBody.isPresent()) {
            hasEmptyBody = false;
        }
        return hasEmptyBody;
    }
