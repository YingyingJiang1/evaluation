    @Override
    public DetailAstImpl visit(ParseTree tree) {
        DetailAstImpl ast = null;
        if (tree != null) {
            ast = tree.accept(this);
        }
        return ast;
    }
