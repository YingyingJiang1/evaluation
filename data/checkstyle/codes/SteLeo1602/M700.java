    private static DetailAST getNeededAssignIdent(DetailAST assignToken) {
        DetailAST assignIdent = assignToken;

        while (traverseUntilNeededBranchType(
            assignIdent, assignToken.getFirstChild(), TokenTypes.IDENT) != null) {

            assignIdent =
                traverseUntilNeededBranchType(assignIdent, assignToken, TokenTypes.IDENT);
        }

        return assignIdent;
    }
