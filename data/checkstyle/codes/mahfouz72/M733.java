    private static List<DetailAST> getBlockStatements(DetailAST statementList) {
        final List<DetailAST> blockStatements = new ArrayList<>();
        DetailAST ast = statementList.getFirstChild();
        while (ast != null) {
            blockStatements.add(ast);
            ast = ast.getNextSibling();
        }
        return Collections.unmodifiableList(blockStatements);
    }
