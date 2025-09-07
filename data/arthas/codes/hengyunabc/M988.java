    public static boolean shouldCompleteOption(Completion completion, String option) {
        List<CliToken> tokens = completion.lineTokens();
        // 有两个 tocken, 然后 倒数第一个不是 - 开头的
        if (tokens.size() >= 2) {
            CliToken cliToken_1 = tokens.get(tokens.size() - 1);
            CliToken cliToken_2 = tokens.get(tokens.size() - 2);
            String token_2 = cliToken_2.value();
            if (!cliToken_1.value().startsWith("-") && token_2.equals(option)) {
                return CompletionUtils.completeClassName(completion);
            }
        }
        // 有三个 token，然后 倒数第一个不是 - 开头的，倒数第2是空的，倒数第3是 --classPattern
        if (tokens.size() >= 3) {
            CliToken cliToken_1 = tokens.get(tokens.size() - 1);
            CliToken cliToken_2 = tokens.get(tokens.size() - 2);
            CliToken cliToken_3 = tokens.get(tokens.size() - 3);
            if (!cliToken_1.value().startsWith("-") && cliToken_2.isBlank()
                    && cliToken_3.value().equals(option)) {
                return CompletionUtils.completeClassName(completion);
            }
        }
        return false;
    }
