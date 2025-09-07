    public static StdoutHandler inject(List<CliToken> tokens) {
        CliToken firstTextToken = null;
        for (CliToken token : tokens) {
            if (token.isText()) {
                firstTextToken = token;
                break;
            }
        }

        if (firstTextToken == null) {
            return null;
        }

        if (firstTextToken.value().equals(GrepHandler.NAME)) {
            return GrepHandler.inject(tokens);
        } else if (firstTextToken.value().equals(PlainTextHandler.NAME)) {
            return PlainTextHandler.inject(tokens);
        } else if (firstTextToken.value().equals(WordCountHandler.NAME)) {
            return WordCountHandler.inject(tokens);
        } else if (firstTextToken.value().equals(TeeHandler.NAME)){
            return TeeHandler.inject(tokens);
        } else{
            return null;
        }
    }
