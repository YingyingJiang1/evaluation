    private static boolean isMethodOfScrapedModule(DetailAST methodDef) {
        final DetailAST classDef = getParentAst(methodDef, TokenTypes.CLASS_DEF);

        boolean isMethodOfModule = false;
        if (classDef != null) {
            final String className = classDef.findFirstToken(TokenTypes.IDENT).getText();
            isMethodOfModule = className.equals(moduleName);
        }

        return isMethodOfModule;
    }
