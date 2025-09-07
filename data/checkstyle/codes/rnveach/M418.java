    private boolean isMissingJavadocAllowed(final DetailAST ast) {
        return allowMissingPropertyJavadoc
                && (isSetterMethod(ast) || isGetterMethod(ast))
            || matchesSkipRegex(ast)
            || isContentsAllowMissingJavadoc(ast);
    }
