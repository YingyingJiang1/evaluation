    public static void completeLongOption(Completion completion, CliToken lastToken, List<Option> options) {
        String prefix = lastToken.value().substring(2);
        List<String> candidates = new ArrayList<String>();
        for (Option option : options) {
            if (option.getLongName().startsWith(prefix)) {
                candidates.add(option.getLongName());
            }
        }
        complete(completion, prefix, candidates);
    }
