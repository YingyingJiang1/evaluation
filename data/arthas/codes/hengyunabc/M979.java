    public static void complete(Completion completion, Class<?> clazz) {
        List<CliToken> tokens = completion.lineTokens();
        CliToken lastToken = tokens.get(tokens.size() - 1);
        CLI cli = CLIConfigurator.define(clazz);
        List<com.taobao.middleware.cli.Option> options = cli.getOptions();
        if (lastToken == null || lastToken.isBlank()) {
            completeUsage(completion, cli);
        } else if (lastToken.value().startsWith("--")) {
            completeLongOption(completion, lastToken, options);
        } else if (lastToken.value().startsWith("-")) {
            completeShortOption(completion, lastToken, options);
        } else {
            completion.complete(Collections.<String>emptyList());
        }
    }
