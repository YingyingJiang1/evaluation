    private static List<CliToken> correctPipeChar(List<CliToken> tokens) {
        List<CliToken> newTokens = new ArrayList<CliToken>(tokens.size()+4);
        for (CliToken token : tokens) {
            String tokenValue = token.value();
            if (tokenValue.length()>1 && tokenValue.endsWith("|")) {
                //split last char '|'
                tokenValue = tokenValue.substring(0, tokenValue.length()-1);
                String rawValue = token.raw();
                rawValue = rawValue.substring(0, rawValue.length()-1);
                newTokens.add(new CliTokenImpl(token.isText(), rawValue, tokenValue));
                //add '|' char
                newTokens.add(new CliTokenImpl(true, "|", "|"));

            } else if (tokenValue.length()>1 && tokenValue.startsWith("|")) {
                //add '|' char
                newTokens.add(new CliTokenImpl(true, "|", "|"));
                //remove first char '|'
                tokenValue = tokenValue.substring(1);
                String rawValue = token.raw();
                rawValue = rawValue.substring(1);
                newTokens.add(new CliTokenImpl(token.isText(), rawValue, tokenValue));
            } else {
                newTokens.add(token);
            }
        }
        return newTokens;
    }
