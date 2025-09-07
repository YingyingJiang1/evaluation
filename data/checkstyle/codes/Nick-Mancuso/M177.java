    @Override
    public void consume(CharStream input) {
        final int currentChar = input.LA(1);
        if (currentChar == '\n') {
            line++;
            charPositionInLine = 0;
        }
        else if (currentChar == '\r') {
            final int nextChar = input.LA(2);
            if (nextChar != '\n') {
                line++;
                charPositionInLine = 0;
            }
        }
        else {
            charPositionInLine++;
        }
        input.consume();
    }
