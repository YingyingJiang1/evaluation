    private boolean matchesSkipRegex(DetailAST methodDef) {
        boolean result = false;
        if (ignoreMethodNamesRegex != null) {
            final DetailAST ident = methodDef.findFirstToken(TokenTypes.IDENT);
            final String methodName = ident.getText();

            final Matcher matcher = ignoreMethodNamesRegex.matcher(methodName);
            if (matcher.matches()) {
                result = true;
            }
        }
        return result;
    }
