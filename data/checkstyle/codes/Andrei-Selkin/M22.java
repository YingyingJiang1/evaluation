    public static FullIdent createFullIdent(DetailAST ast) {
        final FullIdent ident = new FullIdent();
        extractFullIdent(ident, ast);
        return ident;
    }
