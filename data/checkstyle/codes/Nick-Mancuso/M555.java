    private static DetailAST getPreviousElementOfMultiDimArray(DetailAST leftBracket) {
        final DetailAST previousRightBracket = leftBracket.getPreviousSibling().getLastChild();

        DetailAST ident = null;
        // This will get us past the type ident, to the actual identifier
        DetailAST parent = leftBracket.getParent().getParent();
        while (ident == null) {
            ident = parent.findFirstToken(TokenTypes.IDENT);
            parent = parent.getParent();
        }

        final DetailAST previousElement;
        if (ident.getColumnNo() > previousRightBracket.getColumnNo()
                && ident.getColumnNo() < leftBracket.getColumnNo()) {
            // C style and Java style ' int[] arr []' in same construct
            previousElement = ident;
        }
        else {
            // 'int[][] arr' or 'int arr[][]'
            previousElement = previousRightBracket;
        }
        return previousElement;
    }
