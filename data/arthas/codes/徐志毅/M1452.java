    public static String retrievePreviousArg(List<CliToken> tokens, String lastToken) {
        if (StringUtils.isBlank(lastToken) && tokens.size() > 2) {
            // tokens = { " ", "CLASS_NAME", " "}
            return tokens.get(tokens.size() - 2).value();
        } else if (tokens.size() > 3) {
            // tokens = { " ", "CLASS_NAME", " ", "PARTIAL_METHOD_NAME"}
            return tokens.get(tokens.size() - 3).value();
        } else {
            return Constants.EMPTY_STRING;
        }
    }
