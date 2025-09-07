    public static boolean completeOptions(Completion completion, List<OptionCompleteHandler> handlers) {
        List<CliToken> tokens = completion.lineTokens();
        /**
         * <pre>
         * 比如 ` --name a`，这样子的tokens
         * </pre>
         */
        if (tokens.size() >= 3) {
            CliToken cliToken_2 = tokens.get(tokens.size() - 2);
            CliToken cliToken_3 = tokens.get(tokens.size() - 3);

            if (cliToken_2.isBlank()) {
                String token_3 = cliToken_3.value();

                for (OptionCompleteHandler handler : handlers) {
                    if (handler.matchName(token_3)) {
                        return handler.complete(completion);
                    }
                }
            }
        }

        /**
         * <pre>
         * 比如 ` --name `，这样子的tokens
         * </pre>
         */
        if (tokens.size() >= 2) {
            CliToken cliToken_1 = tokens.get(tokens.size() - 1);
            CliToken cliToken_2 = tokens.get(tokens.size() - 2);
            if (cliToken_1.isBlank()) {
                String token_2 = cliToken_2.value();
                for (OptionCompleteHandler handler : handlers) {
                    if (handler.matchName(token_2)) {
                        return handler.complete(completion);
                    }
                }
            }
        }

        return false;
    }
