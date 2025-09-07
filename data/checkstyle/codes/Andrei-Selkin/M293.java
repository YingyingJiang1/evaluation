    private static DetailAST getNearestClassOrEnumDefinition(DetailAST ast) {
        DetailAST searchAST = ast;
        while (searchAST.getType() != TokenTypes.CLASS_DEF
               && searchAST.getType() != TokenTypes.ENUM_DEF) {
            searchAST = searchAST.getParent();
        }
        return searchAST;
    }
