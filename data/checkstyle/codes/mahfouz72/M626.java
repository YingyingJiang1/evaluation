    private static boolean hasStringCaseLabel(DetailAST caseAST) {
        DetailAST curNode = caseAST;
        boolean hasStringCaseLabel = false;
        boolean exitCaseLabelExpression = false;
        while (!exitCaseLabelExpression) {
            DetailAST toVisit = curNode.getFirstChild();
            if (curNode.getType() == TokenTypes.STRING_LITERAL) {
                hasStringCaseLabel = true;
                break;
            }
            while (toVisit == null) {
                toVisit = curNode.getNextSibling();
                curNode = curNode.getParent();
            }
            curNode = toVisit;
            exitCaseLabelExpression = TokenUtil.isOfType(curNode, TokenTypes.COLON,
                                                                        TokenTypes.LAMBDA);
        }
        return hasStringCaseLabel;
    }
