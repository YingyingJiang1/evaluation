        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg, RecognitionException ex) {
            final String message = line + ":" + charPositionInLine + ": " + msg;
            throw new IllegalStateException(message, ex);
        }
