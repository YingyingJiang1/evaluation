    @Override
    public int getColumnNo() {
        int resultNo = -1;

        if (columnNo == NOT_INITIALIZED) {
            // an inner AST that has been initialized
            // with initialize(String text)
            resultNo = findColumnNo(firstChild);

            if (resultNo == -1) {
                resultNo = findColumnNo(nextSibling);
            }
        }
        if (resultNo == -1) {
            resultNo = columnNo;
        }
        return resultNo;
    }
