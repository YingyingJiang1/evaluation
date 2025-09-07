    @Override
    protected final boolean mustCheckName(DetailAST ast) {
        final DetailAST location =
                ast.getParent().getParent();
        return location.getType() == TokenTypes.RECORD_DEF;
    }
