    public static String getShortNameOfAnonInnerClass(DetailAST literalNewAst) {
        DetailAST parentAst = literalNewAst;
        while (TokenUtil.isOfType(parentAst, TokenTypes.LITERAL_NEW, TokenTypes.DOT)) {
            parentAst = parentAst.getParent();
        }
        final DetailAST firstChild = parentAst.getFirstChild();
        return extractQualifiedName(firstChild);
    }
