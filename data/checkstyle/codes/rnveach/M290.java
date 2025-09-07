    private boolean branchContainsJavadocComment(DetailAST token) {
        boolean result = false;
        DetailAST curNode = token;
        while (curNode != null) {
            if (curNode.getType() == TokenTypes.BLOCK_COMMENT_BEGIN
                    && JavadocUtil.isJavadocComment(curNode)) {
                result = hasValidJavadocComment(curNode);
                break;
            }

            DetailAST toVisit = curNode.getFirstChild();
            while (toVisit == null) {
                if (curNode == token) {
                    break;
                }

                toVisit = curNode.getNextSibling();
                curNode = curNode.getParent();
            }
            curNode = toVisit;
        }

        return result;
    }
