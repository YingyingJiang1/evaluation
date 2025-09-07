    private static int getLengthOfBlock(DetailAST openingBrace, DetailAST closingBrace) {
        final int startLineNo = openingBrace.getLineNo();
        final int endLineNo = closingBrace.getLineNo();
        return endLineNo - startLineNo + 1;
    }
