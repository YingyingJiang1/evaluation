    private static Scope getDefaultScope(DetailAST ast) {
        final Scope result;
        if (isInEnumBlock(ast)) {
            if (ast.getType() == TokenTypes.ENUM_CONSTANT_DEF) {
                result = Scope.PUBLIC;
            }
            else if (ast.getType() == TokenTypes.CTOR_DEF) {
                result = Scope.PRIVATE;
            }
            else {
                result = Scope.PACKAGE;
            }
        }
        else if (isInInterfaceOrAnnotationBlock(ast)) {
            result = Scope.PUBLIC;
        }
        else {
            result = Scope.PACKAGE;
        }
        return result;
    }
