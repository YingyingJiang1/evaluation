    private void registerImport(DetailAST imp) {
        final FullIdent ident = FullIdent.createFullIdent(
            imp.getLastChild().getPreviousSibling());
        final String fullName = ident.getText();
        final int lastDot = fullName.lastIndexOf(DOT);
        importedClassPackages.put(fullName.substring(lastDot + 1), fullName);
    }
