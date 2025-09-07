    private static List<DetailAST> getChildList(DetailAST ast) {
        final List<DetailAST> children = new ArrayList<>();
        DetailAST child = ast.findFirstToken(TokenTypes.OBJBLOCK).getFirstChild();
        while (child != null) {
            children.add(child);
            child = child.getNextSibling();
        }
        return children;
    }
