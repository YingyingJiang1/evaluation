    private boolean isForwardReference(DetailAST fieldDef) {
        final DetailAST exprStartIdent = fieldDef.findFirstToken(TokenTypes.IDENT);
        final Set<DetailAST> exprIdents = getAllTokensOfType(exprStartIdent, TokenTypes.IDENT);
        boolean forwardReference = false;
        for (DetailAST ident : exprIdents) {
            if (classFieldNames.contains(ident.getText())) {
                forwardReference = true;
                break;
            }
        }
        return forwardReference;
    }
