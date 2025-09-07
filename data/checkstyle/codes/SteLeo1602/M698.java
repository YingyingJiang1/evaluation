    @Nullable
    private static DetailAST shiftToNextTraversedBranch(DetailAST ast, DetailAST boundAst) {
        DetailAST newAst = ast;

        if (ast.getFirstChild() != null) {
            newAst = ast.getFirstChild();
        }
        else {
            while (newAst.getNextSibling() == null && !newAst.equals(boundAst)) {
                newAst = newAst.getParent();
            }
            if (newAst.equals(boundAst)) {
                newAst = null;
            }
            else {
                newAst = newAst.getNextSibling();
            }
        }

        return newAst;
    }
