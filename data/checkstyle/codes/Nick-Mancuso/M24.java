    private static boolean isArrayTypeDeclaration(DetailAST arrayDeclarator) {
        DetailAST expression = arrayDeclarator;
        while (expression != null) {
            if (expression.getType() == TokenTypes.EXPR) {
                break;
            }
            expression = expression.getFirstChild();
        }
        return expression == null;
    }
