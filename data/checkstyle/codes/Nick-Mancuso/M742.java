    @Override
    public Token recoverInline(Parser recognizer) {
        reportError(recognizer, new InputMismatchException(recognizer));
        return super.recoverInline(recognizer);
    }
