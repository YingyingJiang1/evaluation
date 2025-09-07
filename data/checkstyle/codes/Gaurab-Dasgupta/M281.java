    @Override
    public void finishTree(DetailAST ast) {
        if (ast == null) {
            log(DEFAULT_LINE_NUMBER, MSG_KEY_NO_CODE);
        }
    }
