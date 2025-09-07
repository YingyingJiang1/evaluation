    private static int findLineNo(DetailAST ast) {
        int resultNo = -1;
        DetailAST node = ast;
        while (node != null) {
            // comment node can't be start of any java statement/definition
            if (TokenUtil.isCommentType(node.getType())) {
                node = node.getNextSibling();
            }
            else {
                resultNo = node.getLineNo();
                break;
            }
        }
        return resultNo;
    }
