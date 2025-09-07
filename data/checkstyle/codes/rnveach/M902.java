    @Override
    public int getLineNo() {
        int resultNo = -1;

        if (lineNo == NOT_INITIALIZED) {
            // an inner AST that has been initialized
            // with initialize(String text)
            resultNo = findLineNo(firstChild);

            if (resultNo == -1) {
                resultNo = findLineNo(nextSibling);
            }
        }
        if (resultNo == -1) {
            resultNo = lineNo;
        }
        return resultNo;
    }
