    private static DetailAST getCodeBlockDefinitionToken(DetailAST ident) {
        DetailAST parent = ident;
        while (parent != null
               && parent.getType() != TokenTypes.METHOD_DEF
               && parent.getType() != TokenTypes.STATIC_INIT) {
            parent = parent.getParent();
        }
        return parent;
    }
