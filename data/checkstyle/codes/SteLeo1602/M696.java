    @Nullable
    private static DetailAST getBranchLeadingToReassignedVars(DetailAST ast) {
        DetailAST leadingToReassignedVarBranch = null;

        for (DetailAST conditionalStatement = ast;
             conditionalStatement != null && leadingToReassignedVarBranch == null;
             conditionalStatement = conditionalStatement.getParent()) {

            if (conditionalStatement.getType() == TokenTypes.LITERAL_IF
                || conditionalStatement.getType() == TokenTypes.LITERAL_ELSE) {

                leadingToReassignedVarBranch =
                    conditionalStatement.findFirstToken(TokenTypes.SLIST);

            }
            else if (conditionalStatement.getType() == TokenTypes.QUESTION) {
                leadingToReassignedVarBranch = conditionalStatement;
            }
        }

        return leadingToReassignedVarBranch;

    }
