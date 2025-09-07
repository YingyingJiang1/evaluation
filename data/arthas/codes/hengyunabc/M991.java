    public static List<CliToken> tokenize(String s) {

        List<CliToken> tokens = new LinkedList<CliToken>();

        tokenize(s, 0, tokens);

        tokens = correctPipeChar(tokens);
        return tokens;

    }
