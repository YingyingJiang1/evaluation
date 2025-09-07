    private static boolean isSetterMethod(DetailAST ast) {
        boolean setterMethod = false;

        if (ast.getType() == TokenTypes.METHOD_DEF) {
            final DetailAST type = ast.findFirstToken(TokenTypes.TYPE);
            final String name = type.getNextSibling().getText();
            final Pattern setterPattern = Pattern.compile("^set[A-Z].*");

            setterMethod = setterPattern.matcher(name).matches();
        }
        return setterMethod;
    }
