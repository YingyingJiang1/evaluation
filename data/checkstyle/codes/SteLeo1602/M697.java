    @Nullable
    private static DetailAST traverseUntilNeededBranchType(DetailAST startingBranch,
                              DetailAST bound, int neededTokenType) {

        DetailAST match = null;

        DetailAST iteratedBranch = shiftToNextTraversedBranch(startingBranch, bound);

        while (iteratedBranch != null) {
            if (iteratedBranch.getType() == neededTokenType) {
                match = iteratedBranch;
                break;
            }

            iteratedBranch = shiftToNextTraversedBranch(iteratedBranch, bound);
        }

        return match;
    }
