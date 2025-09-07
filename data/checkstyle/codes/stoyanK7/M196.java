    private static String getAnnotationFullIdent(DetailAST annotationNode) {
        final DetailAST identNode = annotationNode.findFirstToken(TokenTypes.IDENT);
        final String annotationString;

        // If no `IDENT` is found, then we have a `DOT` -> more than 1 qualifier
        if (identNode == null) {
            final DetailAST dotNode = annotationNode.findFirstToken(TokenTypes.DOT);
            annotationString = FullIdent.createFullIdent(dotNode).getText();
        }
        else {
            annotationString = identNode.getText();
        }

        return annotationString;
    }
