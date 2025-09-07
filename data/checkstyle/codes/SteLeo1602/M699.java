    @Nullable
    private static DetailAST getMatchedAssignToken(DetailAST preAssignBranch) {
        DetailAST matchedAssignToken = null;

        for (int assignType : ASSIGN_TOKEN_TYPES) {
            matchedAssignToken = preAssignBranch.findFirstToken(assignType);
            if (matchedAssignToken != null) {
                break;
            }
        }

        return matchedAssignToken;
    }
