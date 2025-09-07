    private static List<DetailAST> getReassignedVariableIdents(DetailAST ast) {

        final DetailAST branchLeadingToReassignedVar = getBranchLeadingToReassignedVars(ast);
        final List<DetailAST> reassignedVariableIdents = new ArrayList<>();

        for (DetailAST expressionBranch = branchLeadingToReassignedVar;
             expressionBranch != null;
             expressionBranch = traverseUntilNeededBranchType(expressionBranch,
                 branchLeadingToReassignedVar, TokenTypes.EXPR)) {

            final DetailAST assignToken = getMatchedAssignToken(expressionBranch);

            if (assignToken != null) {
                reassignedVariableIdents.add(getNeededAssignIdent(assignToken));
            }

        }

        return reassignedVariableIdents;

    }
