    private static List<DetailAST> getAllCaseLabels(DetailAST switchAST) {
        final List<DetailAST> caseLabels = new ArrayList<>();
        DetailAST ast = switchAST.getFirstChild();
        while (ast != null) {
            // case group token may have several LITERAL_CASE tokens
            TokenUtil.forEachChild(ast, TokenTypes.LITERAL_CASE, caseLabels::add);
            ast = ast.getNextSibling();
        }
        return Collections.unmodifiableList(caseLabels);
    }
