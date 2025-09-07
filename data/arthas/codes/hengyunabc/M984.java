    public static void completeShortOption(Completion completion, CliToken lastToken, List<Option> options) {
        String prefix = lastToken.value().substring(1);
        List<String> candidates = new ArrayList<String>();
        for (Option option : options) {
            if (option.getShortName().startsWith(prefix)) {
                candidates.add(option.getShortName());
            }
        }
        complete(completion, prefix, candidates);
    }
