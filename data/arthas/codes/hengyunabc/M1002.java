    public static List<String> parseArgs(List<CliToken> tokens, String command) {
        List<String> args = new LinkedList<String>();
        boolean found = false;
        for (CliToken token : tokens) {
            if (token.isText() && token.value().equals(command)) {
                found = true;
            } else if (token.isText() && found) {
                args.add(token.value());
            }
        }
        return args;
    }
