    public static CliToken getLast(List<CliToken> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        } else {
            return tokens.get(tokens.size() -1);
        }
    }
