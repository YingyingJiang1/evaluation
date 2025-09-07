    public static boolean complete(Completion completion, Collection<String> searchScope) {
        List<CliToken> tokens = completion.lineTokens();
        String lastToken = tokens.get(tokens.size() - 1).value();
        List<String> candidates = new ArrayList<String>();

        if (StringUtils.isBlank(lastToken)) {
            lastToken = "";
        }

        for (String name: searchScope) {
            if (name.startsWith(lastToken)) {
                candidates.add(name);
            }
        }
        if (candidates.size() == 1) {
            completion.complete(candidates.get(0).substring(lastToken.length()), true);
            return true;
        } else {
            completion.complete(candidates);
            return true;
        }
    }
