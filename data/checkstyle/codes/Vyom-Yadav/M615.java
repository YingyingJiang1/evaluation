    private void iterateOverBlockContainingLocalAnonInnerClass(
            DetailAST ast, Deque<VariableDesc> variablesStack) {
        DetailAST currNode = ast;
        while (currNode != null) {
            customVisitToken(currNode, variablesStack);
            DetailAST toVisit = currNode.getFirstChild();
            while (currNode != ast && toVisit == null) {
                customLeaveToken(currNode, variablesStack);
                toVisit = currNode.getNextSibling();
                currNode = currNode.getParent();
            }
            currNode = toVisit;
        }
    }
