    public void initialize(Token token) {
        text = token.getText();
        type = token.getType();
        lineNo = token.getLine();
        columnNo = token.getCharPositionInLine();
    }
