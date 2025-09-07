    private static DetailAST findTokenWhichBeginsTheLine(DetailAST root) {
        final DetailAST tokenWhichBeginsTheLine;
        if (isUsingOfObjectReferenceToInvokeMethod(root)) {
            tokenWhichBeginsTheLine = findStartTokenOfMethodCallChain(root);
        }
        else {
            tokenWhichBeginsTheLine = root.getFirstChild().findFirstToken(TokenTypes.IDENT);
        }
        return tokenWhichBeginsTheLine;
    }
